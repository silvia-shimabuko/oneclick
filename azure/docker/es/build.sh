#!/bin/bash

cd $(dirname $(readlink -f $0))

source commons.sh

docker build --tag $FULLNAME .
