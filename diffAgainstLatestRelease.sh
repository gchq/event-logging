#!/bin/bash

set -e

#API_URL="https://api.github.com/repos/gchq/event-logging/releases/latest"
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
    echo "${RED}INVALID ARGS!${NC}"
    echo "${GREEN}Usage: $0 workingDir previousVersionTag${NC}"
    echo "${GREEN}e.g  : $0 event-logging-api/build v3.2.3_schema-v3.2.4${NC}"

    echo "Where workingDir normally event-logging-api/build"
    exit 1
  fi

  workingDir=${1}
  prevVersionTag="${2}"
  echo "workingDir=${workingDir}"

  echo "Comparing current JAXB code to release ${prevVersionTag}"

  # GH_USER_AND_TOKEN decalred in .travis.yml env:/global/:secure
  # DO NOT echo the token!
  extraCurlArgs=()
  if [[ -n "${GH_USER_AND_TOKEN}" ]]; then 
    # running in travis so use authentication
    extraCurlArgs=( --user "${GH_USER_AND_TOKEN}" )
  fi

  apiUrl="${API_URL_BASE}/${prevVersionTag}"
  prevVersionJar="event-logging-${prevVersionTag}-sources.jar"

  echo "Using API URL: ${apiUrl}"
  echo "Searching for file: ${prevVersionJar}"

  jqScript=".assets[]
      | select( .name 
      | contains(\"${prevVersionJar}\")) 
      | .browser_download_url"

  #echo "Using jqScript: ${jqScript}"

  status_code="$( \
    curl -sL -o /dev/null -w "%{http_code}" "${extraCurlArgs[@]}" "${apiUrl}")"

  if [[ "${status_code}" -ne 200 ]]; then
    curl -sIL "${extraCurlArgs[@]}" "${apiUrl}"
  fi

  curl -s "${extraCurlArgs[@]}" "${apiUrl}" \
    | jq -r ".assets[]" | head -n 200

  # Call the github API to git the json for the latest release, 
  # then extract the sources jar binary url from it
  sourcesJarUrl="$( \
    curl -s "${extraCurlArgs[@]}" "${apiUrl}" \
    | jq -r "${jqScript}" )"

  # Call the github API to git the json for the latest release, 
  # then extract the sources jar binary url from it
  # GH_USER_AND_TOKEN decalred in .travis.yml env:/global/:secure
  #sedScriptArgs=( 's/"browser_download_url":.*"\(http.*event-logging-v.*sources\.jar\)"/\1/p' )
  #extraCurlArgs=()
  #if [ ! "${GH_USER_AND_TOKEN}x" = "x" ]; then 
    ## running in travis so use authentication
    #extraCurlArgs=( --user "${GH_USER_AND_TOKEN}" )
  #fi
  #latestUrl=$( \
    #curl -s "${extraCurlArgs[@]}" ${API_URL} \
    #| sed -ne "${sedScriptArgs[@]}")

  ##echo "latestUrl=${latestUrl}"
  ##echo "PWD=${PWD}"

  #if [ "${latestUrl}x" = "x" ]; then 
    #echo
    #echo -e "${RED}ERROR${NC} Latest sources file url could not be found" \
      #"from ${API_URL} json content:"
    ##dump out all the download urls
    #curl -s ${API_URL} \
      #| grep "\"browser_download_url\""
    #exit 1
  #fi

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

  echo -e "${BLUE}Comparing the source to the latested released version${NC}"

  diff -r old/ new/ > source.diff || true


  if [ "$(wc -l < source.diff)" -gt 0 ]; then
    #cat source.diff
    echo 
    echo -e "\n${RED}Source code differs from ${sourcesJarUrl} see:${NC}" \

    echo -e "${BLUE}meld ${PWD}/old/ ${PWD}/new/${NC}"
    echo
    echo -e "${BLUE}$PWD/source.diff${NC}"

    echo "The nature of the changes will determine whether the next release" \
      "is major/minor/patch or may indicate a bad change to the schema"
  else 
    echo -e "\n${GREEN}Source is identical to ${YELLOW}${sourcesJarUrl}${NC}"
  fi

  exit 0
}

main "$@"
