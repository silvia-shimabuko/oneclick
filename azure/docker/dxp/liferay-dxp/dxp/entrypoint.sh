#!/bin/bash

set -o errexit

main() {
  export ENTRYPOINT_DIR="/docker-init.d"

  entrypoint-process-config-directory.sh
  entrypoint-process-deploy-directory.sh
  entrypoint-process-license-directory.sh
  entrypoint-process-script-directory.sh
  exec entrypoint-run-portal.sh
}

main "$@"
