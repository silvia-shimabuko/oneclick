if [[ ! -e ../../config/userinput.tfvars ]]; then
    echo "userinput.tfvars not found at "$(readlink -f ../../../config/)
    exit 1
fi

source <(cat ../../config/userinput.tfvars|sed 's/ *= */=/g')

REGISTRY_NAME=$registry_name
QUALIFIED_REGISTRY_NAME=$REGISTRY_NAME.azurecr.io
IMAGE_NAME=elasticsearch:6.1.4
FULLNAME=$QUALIFIED_REGISTRY_NAME/$IMAGE_NAME

