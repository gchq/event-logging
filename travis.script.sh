#!/bin/bash

#exit script on any error
set -e

#Shell Colour constants for use in 'echo -e'
#e.g.  echo -e "My message ${GREEN}with just this text in green${NC}"
# shellcheck disable=SC2034
{
  RED='\033[1;31m'
  GREEN='\033[1;32m'
  YELLOW='\033[1;33m'
  BLUE='\033[1;34m'
  NC='\033[0m' # No Colour 
}

#establish what version we are building
if [ -n "$TRAVIS_TAG" ]; then
    #Tagged commit so use that as our version, e.g. v1.2.3
    PRODUCT_VERSION="${TRAVIS_TAG}"

    #upload the maven artefacts to bintray
    EXTRA_BUILD_ARGS="bintrayUpload"
else
    #No tag so use the branch name as the version, e.g. dev-SNAPSHOT
    #None tagged builds are NOT pushed to bintray
    PRODUCT_VERSION="SNAPSHOT"
    EXTRA_BUILD_ARGS=""
fi

#Dump all the travis env vars to the console for debugging
echo -e "TRAVIS_BUILD_NUMBER: [${GREEN}${TRAVIS_BUILD_NUMBER}${NC}]"
echo -e "TRAVIS_COMMIT:       [${GREEN}${TRAVIS_COMMIT}${NC}]"
echo -e "TRAVIS_BRANCH:       [${GREEN}${TRAVIS_BRANCH}${NC}]"
echo -e "TRAVIS_TAG:          [${GREEN}${TRAVIS_TAG}${NC}]"
echo -e "TRAVIS_PULL_REQUEST: [${GREEN}${TRAVIS_PULL_REQUEST}${NC}]"
echo -e "TRAVIS_EVENT_TYPE:   [${GREEN}${TRAVIS_EVENT_TYPE}${NC}]"
echo -e "PRODUCT_VERSION:     [${GREEN}${PRODUCT_VERSION}${NC}]"

#Run the build (including running maven install task to generate poms
./gradlew -Pversion=$PRODUCT_VERSION clean build ${EXTRA_BUILD_ARGS}

echo -e "${GREEN}Now run example application build${NC}"

pushd example-logged-application >/dev/null

./gradlew clean build

popd >/dev/null

exit 0
