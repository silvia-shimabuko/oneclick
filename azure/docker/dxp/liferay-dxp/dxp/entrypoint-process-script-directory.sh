#!/bin/bash

set -o errexit

process_script_directory() {
    echo "##
## Script
##"
  if isempty "$ENTRYPOINT_DIR/script"; then
    echo "No 'script' directory found. If you wish to extra customize Liferay
drop your *.sh files into 'script' directory.

  "
    return 0
  fi

  echo "'script' directory found. The following contents are going to be executed
  "

  tree --noreport $ENTRYPOINT_DIR/script

  for f in $ENTRYPOINT_DIR/script/*; do
    case "$f" in
      *.sh)     echo "Running $f"; . "$f" ;;
      *)        echo "Ignoring $f" ;;
    esac
    echo
  done

  echo "
"
}

process_script_directory "$@"
