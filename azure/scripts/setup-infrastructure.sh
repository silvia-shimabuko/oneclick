#!/bin/bash

set -e
SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )
cd  $SCRIPT_DIR

source $SCRIPT_DIR/commons.sh

function main() {
    validate_setup
    prepare_state_backend

    rm -rf "$SCRIPT_DIR/tmp"

    terraform_apply infrastructure
    ../docker/es/build.sh
    docker_push "elasticsearch:6.1.4"
    terraform_apply elasticsearch
}

main
