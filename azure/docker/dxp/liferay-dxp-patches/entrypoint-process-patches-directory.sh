#!/bin/bash

set -o errexit

process_patches_directory() {
    echo "##
## Patches
##"
  if isempty "$ENTRYPOINT_DIR/patches"; then
    echo "No 'patches' directory found. If you wish to apply patches and hot-fixes to Liferay DXP
drop your patches (*.zip) in the 'patches' directory.

  "
    return 0
  fi

  echo "'patches' directory found. The following content are going to be copied to $LIFERAY_HOME/patching-tool/patches/
"
  if [ ! -d $LIFERAY_HOME/patching-tool ]; then
      echo "No '$LIFERAY_HOME/patching-tool' directory found."
      return 0
  fi

  tree --noreport $ENTRYPOINT_DIR/patches/
  if [[ -e $ENTRYPOINT_DIR/patching-tool/patching-tool.zip ]]; then
    echo "Found a given patching tool version. Updating patching tool"
    cp $LIFERAY_HOME/patching-tool/default.properties /tmp
    rm -rf $LIFERAY_HOME/patching-tool/
    cd  $LIFERAY_HOME
    unzip $ENTRYPOINT_DIR/patching-tool/patching-tool.zip
    cp /tmp/default.properties patching-tool
    cd -
  fi

  cp -r $ENTRYPOINT_DIR/patches/* $LIFERAY_HOME/patching-tool/patches/

  echo "running patching-tool"
  bash -c "
  cd $LIFERAY_HOME/patching-tool/
  chmod +x patching-tool.sh
  echo 'running install'
  ./patching-tool.sh install
"
}

process_patches_directory "$@"
