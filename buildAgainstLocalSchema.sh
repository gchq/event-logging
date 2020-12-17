#!/usr/bin/env bash

# Builds the event-logging-schema build to create the -client
# variant of the schema based on whatever version of event-logging-schema
# is checked out. Then builds event-logging using that -client schema.

# NOTE: Relies on the two repos being siblings of each other.

set -e

setup_echo_colours() {
  # Exit the script on any error
  set -e

  # shellcheck disable=SC2034
  if [ "${MONOCHROME}" = true ]; then
    RED=''
    GREEN=''
    YELLOW=''
    BLUE=''
    BLUE2=''
    DGREY=''
    NC='' # No Colour
  else 
    RED='\033[1;31m'
    GREEN='\033[1;32m'
    YELLOW='\033[1;33m'
    BLUE='\033[1;34m'
    BLUE2='\033[1;34m'
    DGREY='\e[90m'
    NC='\033[0m' # No Colour
  fi
}

debug_value() {
  local name="$1"; shift
  local value="$1"; shift
  
  if [ "${IS_DEBUG}" = true ]; then
    echo -e "${DGREY}DEBUG ${name}: ${value}${NC}"
  fi
}

debug() {
  local str="$1"; shift
  
  if [ "${IS_DEBUG}" = true ]; then
    echo -e "${DGREY}DEBUG ${str}${NC}"
  fi
}

main() {
  IS_DEBUG=false

  setup_echo_colours

  # Build the client schema from the master event-logging.xsd
  pushd ../event-logging-schema >/dev/null
  ./gradlew clean build -x diffAgainstLatest 
  popd >/dev/null

  ./gradlew \
    clean \
    build \
    -PschemaFilePath=../event-logging-schema/event-logging-transformer-main/pipelines/generated/event-logging-v3-client.xsd \
    -x diffAgainstLatest \
    publishToMavenLocal

  echo -e "${GREEN}Done${NC}"
}

main "$@"

