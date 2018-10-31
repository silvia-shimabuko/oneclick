#!/usr/bin/env bash

# checks if a folder doesn't exists of if is empty and returns 0, otherwise returns 1

is_empty() {
  if [[ ! -d "$1" ]]; then
    return 0
  fi
  if [ ! "$(ls -A "$1")" ]; then
    return 0
  fi
  return 1
}

is_empty "$@"