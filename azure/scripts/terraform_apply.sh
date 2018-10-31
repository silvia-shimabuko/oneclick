#!/bin/bash

set -e
SCRIPT_DIR=$( cd "${BASH_SOURCE%/*}"; printf "$PWD" )
source $SCRIPT_DIR/commons.sh

terraform_apply "$1"
