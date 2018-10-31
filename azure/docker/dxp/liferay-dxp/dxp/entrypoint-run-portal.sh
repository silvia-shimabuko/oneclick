#!/bin/bash

set -o errexit

init_data() {
  mkdir -p $LIFERAY_HOME/data
  if [ -z "$(ls -A $LIFERAY_HOME/data)" ]; then
    cp -r /docker-init.d/data/* $LIFERAY_HOME/data/
  fi
}

run_portal() {
  exec catalina.sh run
}

init_data
run_portal "$@"
