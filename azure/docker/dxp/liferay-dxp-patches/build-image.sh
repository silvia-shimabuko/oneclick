#!/bin/bash

set -e

cd $(dirname $(readlink -f $0))

VERSION_NAME_DXP=$1

#docker build --build-arg DXP_IMAGE=liferay/liferay-dxp:7.1.10 --tag liferay/liferay-dxp:7.1.10-hotfix1 .
echo "docker build --build-arg DXP_IMAGE=liferay/liferay-dxp:$VERSION_NAME_DXP --tag liferay/liferay-dxp:$VERSION_NAME_DXP-hotfix1 ."
docker build --build-arg DXP_IMAGE=liferay/liferay-dxp:$VERSION_NAME_DXP --tag liferay/liferay-dxp:$VERSION_NAME_DXP-hotfix1 .

