#!/bin/bash

set -o errexit

process_license_directory() {
    echo "##
## License
##"
  if isempty "$ENTRYPOINT_DIR/license"; then
    echo "No 'license' directory found. If you wish to provide Liferay a license make sure
to drop your *.xml or *.aatf files in the 'license' directory.

  "
    return 0
  fi

  echo "'license' directory found. The following contents are going to be copied to $LIFERAY_HOME/deploy and/or $LIFERAY_HOME/data:
"

  tree --noreport $ENTRYPOINT_DIR/license
  mkdir -p $LIFERAY_HOME/deploy
  cp -r $ENTRYPOINT_DIR/license/*.xml $LIFERAY_HOME/deploy 2>/dev/null || true
  mkdir -p $LIFERAY_HOME/data
  cp -r $ENTRYPOINT_DIR/license/*.aatf $LIFERAY_HOME/data 2>/dev/null || true

  rm -rf $LIFERAY_HOME/licenses
  echo "
"
}

process_license_directory "$@"