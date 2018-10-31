#!/usr/bin/env bash

set -o errexit
set -o nounset

if [[ ! -e ../config/userinput.tfvars ]]; then
    echo "userinput.tfvars not found at "$(readlink -f ../config/)
    exit 1
fi

source <(cat ../config/userinput.tfvars|sed 's/ *= */=/g')

SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )
cd  $SCRIPT_DIR

source $SCRIPT_DIR/commons.sh

VERSION=$(date +"%s")

echo "## Proxy image build"
../docker/proxy/build.sh $VERSION
docker_push $proxy_image_name $proxy_image_name:$VERSION

echo "## Portal image build"
../docker/dxp/build-dxp-customizations.sh $VERSION
docker_push $dxp_image_name $dxp_image_name:$VERSION