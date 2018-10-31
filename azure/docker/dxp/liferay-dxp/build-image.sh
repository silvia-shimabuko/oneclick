#!/bin/bash

set -e
set -o errexit

cd $(dirname $(readlink -f $0))
DIR=$(pwd)

if [ -z "$1" ]; then
  echo "Error: must specify a version:"
  for VERSION in `find . -name "env*.sh" | grep -Po "(?<=./env).+(?=\.sh)" | sort`; do
    echo "- $VERSION"
  done
  exit 1
elif [ ! -f "$DIR/env$1.sh" ]; then
  echo "Error: version not found"
  exit 1
fi

source "$DIR/env$1.sh"

readonly IMAGE_NAME="liferay-dxp"
readonly IMAGE_PREFIX="${IMAGE_PREFIX:-liferay}"
readonly IMAGE_TAG="${IMAGE_TAG:-$VERSION}"

main() {
  cry_if_absent
  build_image
}

cry_if_absent() {
  if [[ ! -f "$DXP_FILENAME" ]]; then
    echo "You have $DXP_FILENAME present to build this project. You can download it from the customer portal"
    exit 1
  fi
}

build_image() {
  echo "execute this command - silvia"
  echo "docker build --build-arg DXP_FILENAME=$DXP_FILENAME --tag $IMAGE_PREFIX/$IMAGE_NAME:$IMAGE_TAG $DIR"
  docker build --build-arg "DXP_FILENAME=$DXP_FILENAME" --tag "$IMAGE_PREFIX/$IMAGE_NAME:$IMAGE_TAG" $DIR
}

main
