#!/bin/bash

set -e
SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )

source $SCRIPT_DIR/commons.sh

function main() {
    terraform_init_with_helm dxp
    rm -rf "$SCRIPT_DIR/tmp"
    terraform_apply elasticsearch
}

main
