#!/bin/bash

set -e

VERSION=$1

cd $(dirname $(readlink -f $0))

echo "# Building the vanilla image"
#liferay-dxp/build-image.sh 7.1.10
NAME_VERSION_ZIP=$(find . -name "env*.sh" | grep -Po "(?<=./env).+(?=\.sh)" | sort)
echo "NAME_VERSION_ZIP $NAME_VERSION_ZIP"
liferay-dxp/build-image.sh  $NAME_VERSION_ZIP

echo "# Building the patched image"
liferay-dxp-patches/build-image.sh $NAME_VERSION_ZIP

echo "# Building the customized portal"
liferay-dxp-customizations/build.sh $VERSION $NAME_VERSION_ZIP
