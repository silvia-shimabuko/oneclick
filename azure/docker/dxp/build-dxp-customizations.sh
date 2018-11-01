#!/bin/bash

set -e

VERSION=$1

cd $(dirname $(readlink -f $0))

NAME_VERSION_ZIP=$(find . -name "env*.sh" | grep -Po "(?<=./env).+(?=\.sh)" | sort)
liferay-dxp/build-image.sh $NAME_VERSION_ZIP

echo "# Building the patched image"
liferay-dxp-patches/build-image.sh $NAME_VERSION_ZIP

echo "# Building the customized portal"
liferay-dxp-customizations/build.sh $VERSION $NAME_VERSION_ZIP