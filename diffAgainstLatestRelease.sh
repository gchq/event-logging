#!/bin/bash

set -eo pipefail

API_URL_BASE="https://api.github.com/repos/gchq/event-logging/releases/tags"

#Shell Colour constants for use in 'echo -e'
RED='\033[1;31m'
GREEN='\033[1;32m'
YELLOW='\033[1;33m'
BLUE='\033[1;34m'
NC='\033[0m' # No Color

pushd() {
  command pushd "$@" > /dev/null
}

popd() {
  command popd > /dev/null
}

main() {
  #Check script arguments
  if [ "$#" -ne 2 ] || ! [ -d "$1" ]; then
    echo -e "${RED}INVALID ARGS!${NC}"
    echo -e "${GREEN}Usage: $0 workingDir previousVersionTag${NC}"
    echo -e "${GREEN}e.g  : $0 event-logging-api/build v3.2.3_schema-v3.2.4${NC}"

    echo -e "Where workingDir is normally event-logging-api/build"
    exit 1
  fi

  local workingDir=${1}

  # The version tag in github (with a v prefix)
  local prevVersionTag="${2}"

  # Remove the v prefix to get the maven version
  local mavenVersion=
  # shellcheck disable=SC2001

  echo -e "${BLUE}Working directory: ${YELLOW}${workingDir}${NC}"

  echo -e "${BLUE}Comparing current JAXB code to release:" \
    "${YELLOW}${prevVersionTag}${NC}"

  # GITHUB_TOKEN decalred in travis settings UI
  # DO NOT echo the token!
  local extraCurlArgs=()
  if [[ -n "${GITHUB_TOKEN}" ]]; then 
    # running in travis so use authentication
    extraCurlArgs=( -H "Authorization: token ${GITHUB_TOKEN}" )
  fi

  local apiUrl="${API_URL_BASE}/${prevVersionTag}"

  echo -e "${BLUE}Using API URL: ${YELLOW}${apiUrl}${NC}"
  echo -e "${BLUE}Searching for Sources JAR"

  local jqScript=".assets[]
      | select( .name 
      | endswith(\"-sources.jar\")) 
      | .browser_download_url"

  #echo "Using jqScript: ${jqScript}"

  local status_code
  status_code="$( \
    curl "${extraCurlArgs[@]}" -sL -o /dev/null -w "%{http_code}" "${apiUrl}" )"

  if [[ "${status_code}" -ne 200 ]]; then
    curl -sIL "${extraCurlArgs[@]}" "${apiUrl}"
  fi

  # Call the github API to git the json for the latest release, 
  # then extract the sources jar binary url from it
  local sourcesJarUrl
  sourcesJarUrl="$( \
    curl -s "${extraCurlArgs[@]}" "${apiUrl}" \
    | jq -r "${jqScript}" )"

  pushd "${workingDir}"

  mkdir -p old
  mkdir -p new

  echo -e "${BLUE}Clearing out old and new directories${NC}"
  rm -rf ./old/*
  rm -rf ./new/*

  pushd old
  #Need -L to follow re-directs as github will redirect the request
  echo -e "${BLUE}Downloading latest source jar from ${YELLOW}${sourcesJarUrl}${NC}"
  curl -s -L -O "${sourcesJarUrl}" 
  unzip -q ./*.jar "event/logging/*.java"
  rm event-logging*.jar
  popd

  pushd new
  cp ../libs/event-logging*-sources.jar ./
  unzip -q ./*.jar "event/logging/*.java"
  rm event-logging*.jar
  popd

  echo -e "${BLUE}Comparing the source to version" \
    "${YELLOW}${prevVersionTag}${NC}"

  diff -r old/ new/ > source.diff || true


  if [ "$(wc -l < source.diff)" -gt 0 ]; then
    #cat source.diff
    echo 
    echo -e "\n${RED}Source code differs from ${sourcesJarUrl} see:${NC}" \

    echo -e "${BLUE}meld ${PWD}/old/ ${PWD}/new/${NC}"
    echo
    echo -e "${BLUE}$PWD/source.diff${NC}"

    echo -e "The nature of the changes will determine whether the next release" \
      "is major/minor/patch or may indicate a bad change to the schema"
  else 
    echo -e "\n${GREEN}Source is identical to ${YELLOW}${sourcesJarUrl}${NC}"
  fi

  exit 0
}

main "$@"
