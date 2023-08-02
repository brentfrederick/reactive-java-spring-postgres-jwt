#!/bin/sh

export ACTIVE_PROFILE="local"

command="./gradlew bootRun"

while getopts 'dc' OPTION; do
  case "$OPTION" in
    d)
      command+=" --debug-jvm"
      ;;
    c)
      command+=" --args='--clean'"
      ;;
    ?)
      echo "script usage: ./start.sh [-d] debug [-c] clean database" >&2
      exit 1
      ;;
  esac
done

echo Running $command

eval $command