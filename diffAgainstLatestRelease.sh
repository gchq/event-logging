#!/bin/bash

set -e

API_URL="https://api.github.com/repos/gchq/event-logging/releases/latest"

#Check script arguments
if [ "$#" -ne 1 ] || ! [ -d "$1" ]; then
    echo "INVALID ARGS!"
    exit 1
fi

workingDir=$1

#Call the github API to git the json for the latest release, then extract the sources jar binary url from it
#latestUrl=$(curl -s ${API_URL} | sed -ne 's/"browser_download_url": "\(http.*event-logging-v.*\sources.jar\)/\1/p' | sed 's/".*$//')
latestUrl=$(curl -s ${API_URL} | sed -ne 's/"browser_download_url": "\(http.*event-logging-v.*\.jar\)/\1/p' | sed 's/".*$//')

echo "latestUrl=${latestUrl}"
echo "workingDir=${workingDir}"

if [ "$latestUrlx" = "x" ]; then 
    echo "ERROR Latest sources file could not be found"
    exit 1
fi


pushd ${workingDir}

mkdir -p old
mkdir -p new

echo "Clearing out old and new directories"
rm -rf ./old/*
rm -rf ./new/*

pushd old
#Need -L to follow re-directs as github will redirect the request
curl -L -O ${latestUrl} 
unzip -q *.jar "event/logging/*.class"
rm event-logging*.jar
popd

pushd new
cp ../libs/event-logging*-sources.jar ./
unzip -q *.jar "event/logging/*.java"
rm event-logging*.jar
popd

echo "Comparing the source to the latested released version"

diff -q -r old/ new/ || echo -e "\n Source is identical"

echo "The full diff can be found in ${workingDir}/source.diff"

diff -r old/ new/ > source.diff


