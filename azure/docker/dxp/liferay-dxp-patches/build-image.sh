#!/bin/bash

set -e

cd $(dirname $(readlink -f $0))

docker build --build-arg DXP_IMAGE=liferay/liferay-dxp:7.1.10 --tag liferay/liferay-dxp:7.1.10-hotfix1 .

