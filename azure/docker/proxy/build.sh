#!/bin/bash

cd $(dirname $(readlink -f $0))

VERSION=$1

source commons.sh

DOCKER_FILE=http.Dockerfile
if [[ ../../config/certs/cert.pem ]]; then
    echo "Ssl certificate found. Building https proxy"
    DOCKER_FILE=https.Dockerfile
else
    echo "Ssl certificate not found. Building plain http proxy"
fi
docker build --tag $FULLNAME:$VERSION --tag $FULLNAME -f $DOCKER_FILE .
