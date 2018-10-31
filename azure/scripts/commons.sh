#!/bin/bash

WORKSPACE_DIR=$( cd "$SCRIPT_DIR/../terraform/workspace"; printf "$PWD" )
CONFIG_DIR=$(readlink -f $SCRIPT_DIR/../config/ )
USER_INPUT=$CONFIG_DIR/userinput.tfvars
USER_PROPERTIES_FILE=$CONFIG_DIR/portal-ext.user.properties
OUT_DIR=$CONFIG_DIR/out
BACKEND_OUT=$OUT_DIR/backend.json

function download_helm {
  if [ ! -f "$SCRIPT_DIR"/tmp/terraform-provider-helm*.tar.gz ]; then
  (
    echo "downloading terraform helm plugin"
    mkdir -p "$SCRIPT_DIR/tmp"
    cd "$SCRIPT_DIR/tmp"
    wget -q https://github.com/mcuadros/terraform-provider-helm/releases/download/v0.5.1/terraform-provider-helm_v0.5.1_linux_amd64.tar.gz
    tar -xf terraform-provider-helm*.tar.gz
  )
  fi
}

function terraform_init {
  MODULE_NAME=$1
  generate_backend_provider $MODULE_NAME

  (
    cd "$WORKSPACE_DIR/$MODULE_NAME"
    terraform init
  )
}

function terraform_init_with_helm {
  MODULE_NAME=$1

  if [ ! -f "$WORKSPACE_DIR/$MODULE_NAME/.terraform/plugins/linux_amd64/terraform-provider-helm" ]; then
    download_helm
    mkdir -p "$WORKSPACE_DIR/$MODULE_NAME/.terraform/plugins/linux_amd64/"
    cp "$SCRIPT_DIR"/tmp/terraform-provider-helm*/terraform-provider-helm "$WORKSPACE_DIR/$MODULE_NAME/.terraform/plugins/linux_amd64/"
  fi

  terraform_init $MODULE_NAME
}

function terraform_apply {
  MODULE_NAME=$1
  shift

  terraform_init_with_helm $MODULE_NAME
  (
    cd "$WORKSPACE_DIR/$MODULE_NAME"
    terraform apply -auto-approve -var-file=$USER_INPUT $@
  )
}

function terraform_plan {
  MODULE_NAME=$1

  terraform_init_with_helm $MODULE_NAME
  (
    cd "$WORKSPACE_DIR/$MODULE_NAME"
    terraform plan -var-file=$USER_INPUT
  )
}


function terraform_destroy {
  MODULE_NAME=$1

  terraform_init_with_helm $MODULE_NAME
  (
    cd "$WORKSPACE_DIR/$MODULE_NAME"
    terraform destroy -auto-approve -var-file=$USER_INPUT
  )
}

function terraform_refresh {
  MODULE_NAME=$1
  shift

  terraform_init_with_helm $MODULE_NAME
  (
    cd "$WORKSPACE_DIR/$MODULE_NAME"
    terraform refresh -var-file=$USER_INPUT
  )
}


function prepare_state_backend() {
    if [[ -e $BACKEND_OUT ]]; then
        return
    fi
    terraform_init  backend
    terraform_apply backend
}

function backend_for_module() {
    echo $WORKSPACE_DIR/$MODULE/backend-provider.tf
}

function get_field() {
    $SCRIPT_DIR/JSON.sh -b < $BACKEND_OUT -b | grep $1 | cut -f2 | sed 's/"//g'
}

function generate_backend_provider() {
    MODULE=$1
    if [[ $MODULE = "backend" ]]; then
        return
    fi
    prepare_state_backend
    BACKEND_FILE=$(backend_for_module  $MODULE)
    if [[ -e $BACKEND_FILE ]]; then
        return
    fi

    STORAGE_ACCOUNT_NAME=$(get_field storage_account_name)
    STORAGE_CONTAINER_NAME=$(get_field container_name)
    STATE_FILE_NAME=$MODULE.tstate
    ACCESS_KEY=$(get_field access_key)

    cat <<EOF > $(backend_for_module $MODULE)
terraform {
  required_version = ">= 0.11.7"
  backend "azurerm" {
    storage_account_name = "${STORAGE_ACCOUNT_NAME}"
    container_name       = "${STORAGE_CONTAINER_NAME}"
    key                  = "${STATE_FILE_NAME}"
    access_key           = "${ACCESS_KEY}"
  }
}
EOF
}

function validate_setup() {
    if [[ ! -e $USER_INPUT ]]; then
        cat <<EOF 2>&1
You must create the following file:
    $USER_INPUT

You can start copying the sample:
    $USER_INPUT.sample

And filling the values as follows as described in the config/README.md document:

$(cat $SCRIPT_DIR/../config/README.md)

EOF
        error "$0 didn't execute. Read instructions"
        exit -1
    fi
}

function error() {
  if has_color_support; then
    echo -e "\033[31m[ERROR] $@\033[0m" 2>&1
  else
    echo "[ERROR] $@" 2>&1
  fi
}

function has_color_support() {
  if ! tput colors>/dev/null 2>&1; then
    return 127
  fi
}

function update_portal() {
    update_portal_config
    terraform_init_with_helm dxp
    rm -rf "$SCRIPT_DIR/tmp"
    upload_certs
    terraform_apply dxp $@
}

function upload_certs() {
    if [[ ! -e $CONFIG_DIR/certs/cert.pem ]]; then
        return
    fi
    echo "Found ssl certificates. Uploading..."

    source <(cat ../config/userinput.tfvars|sed 's/ *= */=/g')

    export AZURE_STORAGE_ACCOUNT=$azure_file_account_name
    eval AZURE_STORAGE_ACCESS_KEY=$(./JSON.sh -b  < ../config/out/azurefile.json | grep access_key | awk '{print $2}')
    export AZURE_STORAGE_ACCESS_KEY
    eval SHARE_NAME=$(./JSON.sh -b  < ../config/out/proxyfile.json | grep proxy_storage_sharename | awk '{print $2}')

    for FILE in ../config/certs/*; do
        echo "Uploading certificate file $FILE"
        az storage file upload --share-name ${SHARE_NAME} --source $FILE
    done
}

function getfield {
    grep "$1" | sed 's/^[^=]*=//'
}

function update_portal_config() {
  echo "Update user configuration..."

  source <(cat ../config/userinput.tfvars|sed 's/ *= */=/g')

  setup_user_properties

  TMP_DIR=$(mktemp -d /tmp/PORTAL_XXXXXX)

  cat templates/portal-ext.user.properties.template | sed -e "s#\${DB_URL}#$DB_URL#" -e "s/\${DB_USER}/$DB_USER/" -e "s/\${DB_PASS}/$DB_PASS/"  > ${TMP_DIR}/portal-ext.user.properties

  kubectl delete configmap dxp-userconfig || true
  kubectl create configmap dxp-userconfig --from-file=${TMP_DIR}/portal-ext.user.properties
}

function setup_user_properties() {

  source <(cat ../config/userinput.tfvars|sed 's/ *= */=/g')

  if [[ ! -e $USER_PROPERTIES_FILE ]]; then
    cat <<EOF 2>&1
You must create the following file:
    $USER_PROPERTIES_FILE

You can start copying the sample:
    $USER_PROPERTIES_FILE.sample

And filling the values as follows as described in the README.md document, if necessary:

$(cat $SCRIPT_DIR/../README.md)

EOF
    error "$0 didn't execute. Read instructions"
    exit -1
  fi

  DB_URL=$db_server_name.postgres.database.azure.com:5432/$db_name
  DB_USER=$db_admin_username@$db_server_name
  DB_PASS=$db_password

  OVERRIDE_DB_URL=$(cat $USER_PROPERTIES_FILE  | getfield jdbc.default.url )
  OVERRIDE_DB_USER=$(cat $USER_PROPERTIES_FILE | getfield jdbc.default.username )
  OVERRIDE_DB_PASS=$(cat $USER_PROPERTIES_FILE | getfield jdbc.default.password )

  export DB_URL=${OVERRIDE_DB_URL:-$DB_URL}
  export DB_USER=${OVERRIDE_DB_USER:-$DB_USER}
  export DB_PASSWORD=${OVERRIDE_DB_PASS:-$DB_PASSWORD}

}

function docker_push() {

  source <(cat ../config/userinput.tfvars|sed 's/ *= */=/g')

  REGISTRY_NAME=$registry_name
  QUALIFIED_REGISTRY_NAME=$REGISTRY_NAME.azurecr.io
  az acr login --name $REGISTRY_NAME

  for img in "$@"; do
    IMAGE_NAME=$img
    FULLNAME=$QUALIFIED_REGISTRY_NAME/$IMAGE_NAME
    docker push $FULLNAME
  done
}