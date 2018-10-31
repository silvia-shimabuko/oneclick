#!/bin/bash

set -e
SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )

source $SCRIPT_DIR/commons.sh

function main() {
    update_portal -var "dxp_replica_count=1"
}

main
