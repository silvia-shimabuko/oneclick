#!/bin/bash

set -o errexit

process_deploy_directory() {
    echo "##
## Deploy
##"
  mkdir -p $LIFERAY_HOME/deploy

  if isempty "$ENTRYPOINT_DIR/deploy"; then
    echo "No 'deploy' directory found. If you wish to deploy custom jar/war/lpkg 
drop your custom modules in the 'deploy' directory.

  "

    return 0
  fi

  echo "'deploy' directory found. The following contents are going to be copied to '$LIFERAY_HOME/deploy':
  "

  tree --noreport $ENTRYPOINT_DIR/deploy
  cp -r $ENTRYPOINT_DIR/deploy/* $LIFERAY_HOME/deploy

  echo "
"
}

process_deploy_directory "$@"