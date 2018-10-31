#!/bin/bash
set -e

cd $(dirname $(readlink -f $0))

VERSION=$1

source commons.sh

docker build --tag $FULLNAME:$VERSION --build-arg DXP_IMAGE=liferay/liferay-dxp:7.1.10-hotfix1 --tag $FULLNAME --build-arg DXP_IMAGE=liferay/liferay-dxp:7.1.10-hotfix1 .
