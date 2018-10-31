#!/bin/bash

# this file should be sourced

SCRIPT_DIR=$( dirname $(readlink -f -- $0))
source $SCRIPT_DIR/commons.sh

export KUBECONFIG=$OUT_DIR/kubeconfig.yaml
