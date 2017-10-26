#!/bin/bash

set -e

API_URL="https://api.github.com/repos/gchq/event-logging/releases/latest"

#Shell Colour constants for use in 'echo -e'
RED='\033[1;31m'
GREEN='\033[1;32m'
YELLOW='\033[1;33m'
BLUE='\033[1;34m'
NC='\033[0m' # No Color

pushd () {
    command pushd "$@" > /dev/null
}

popd () {
    command popd "$@" > /dev/null
}

#Check script arguments
if [ "$#" -ne 1 ] || ! [ -d "$1" ]; then
    echo "${RED}INVALID ARGS!${NC}"
    echo "${GREEN}Usage: $0 workingDir${NC}"
    echo "Where workingDir normally event-logging-api/build"
    exit 1
fi

workingDir=$1

#Call the github API to git the json for the latest release, then extract the sources jar binary url from it
#latestUrl=$(curl -s ${API_URL} | sed -ne 's/"browser_download_url": "\(http.*event-logging-v.*\sources.jar\)/\1/p' | sed 's/".*$//')
latestUrl=$(curl -s ${API_URL} | sed -ne 's/"browser_download_url": "\(http.*event-logging-v.*\.jar\)/\1/p' | sed 's/".*$//')

echo "latestUrl=${latestUrl}"
echo "workingDir=${workingDir}"
echo "PWD=${PWD}"

if [ "$latestUrlx" = "x" ]; then 
    echo -e "${RED}ERROR${NC} Latest sources file could not be found"
    exit 1
fi


pushd ${workingDir}

mkdir -p old
mkdir -p new

echo -e "${BLUE}Clearing out old and new directories${NC}"
rm -rf ./old/*
rm -rf ./new/*

pushd old
#Need -L to follow re-directs as github will redirect the request
echo -e "${BLUE}Downloading latest source jar from ${YELLOW}${latestUrl}${NC}"
curl -s -L -O ${latestUrl} 
unzip -q *.jar "event/logging/*.class"
rm event-logging*.jar
popd

pushd new
cp ../libs/event-logging*-sources.jar ./
unzip -q *.jar "event/logging/*.java"
rm event-logging*.jar
popd

echo -e "${BLUE}Comparing the source to the latested released version${NC}"

diff -r old/ new/ > source.diff || true

if [ $(cat source.diff | wc -l) -gt 0 ]; then
    echo -e "\n${RED}Source code is different (see $PWD/source.diff)${NC}"
    cat source.diff
else 
    echo -e "\n ${GREEN}Source is identical${NC}"
fi

echo -e "${BLUE}Finished diff-ing source code${NC}"
exit 0
