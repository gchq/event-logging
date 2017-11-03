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
#GH_USER_AND_TOKEN decalred in .travis.yml env:/global/:secure
sedScript='s/"browser_download_url":.*"\(http.*event-logging-v.*sources\.jar\)"/\1/p'
if [ "${GH_USER_AND_TOKEN}x" = "x" ]; then 
    #running locally so do it unauthenticated
    latestUrl=$(curl -s ${API_URL} | sed -ne ${sedScript})
else
    latestUrl=$(curl -s --user "${GH_USER_AND_TOKEN}" ${API_URL} | sed -ne ${sedScript})
fi

echo "latestUrl=${latestUrl}"
echo "workingDir=${workingDir}"
echo "PWD=${PWD}"

if [ "${latestUrl}x" = "x" ]; then 
    echo
    echo -e "${RED}ERROR${NC} Latest sources file url could not be found from ${API_URL} json content:"
    #dump out all the download urls
    curl -s ${API_URL} | grep "\"browser_download_url\""
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
unzip -q *.jar "event/logging/*.java"
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
    cat source.diff
    echo -e "\n${RED}Source code differs from ${latestUrl} (see changes above or in $PWD/source.diff)${NC}"
    echo "The nature of the changes will determine whether the next release is major/minor/patch or may indicate a bad change to the schema"
else 
    echo -e "\n${GREEN}Source is identical to ${YELLOW}${latestUrl}${NC}"
fi

exit 0
