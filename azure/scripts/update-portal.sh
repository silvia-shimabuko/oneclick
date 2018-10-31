#!/bin/bash

set -e
SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )

source $SCRIPT_DIR/commons.sh
source kube-context.sh

function main() {
    update_portal
}

main
