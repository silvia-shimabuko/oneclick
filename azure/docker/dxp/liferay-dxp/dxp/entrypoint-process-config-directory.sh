#!/bin/bash

set -o errexit

process_config_directory() {
    echo "##
## Config
##"
  if isempty "$ENTRYPOINT_DIR/config"; then
    echo "No 'config' directory found. If you wish to configure Liferay make sure
to drop your *.properties and *.config files in the 'config' directory.

  "
    return 0
  fi

  echo "'config' directory found. The following contents are going to be copied to $LIFERAY_HOME and/or $LIFERAY_HOME/osgi/configs:
"

  tree --noreport $ENTRYPOINT_DIR/config
  cp -r $ENTRYPOINT_DIR/config/portal-*.properties $LIFERAY_HOME/ 2>/dev/null || true
  mkdir -p $LIFERAY_HOME/osgi/configs
  cp -r $ENTRYPOINT_DIR/config/*.{cfg,config} $LIFERAY_HOME/osgi/configs 2>/dev/null || true

  echo "
"
}

process_config_directory "$@"