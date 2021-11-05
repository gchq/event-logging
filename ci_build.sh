#!/bin/bash

#exit script on any error
set -eo pipefail

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

RELEASE_ARTEFACTS_DIR="${BUILD_DIR}/release_artefacts"
RELEASE_MANIFEST="${RELEASE_ARTEFACTS_DIR}/release-artefacts.txt"
GH_PAGES_DIR="$BUILD_DIR/gh-pages"

build_example_app() {
  echo -e "${GREEN}Now run example application build${NC}"

  pushd example-logged-application >/dev/null

  ./gradlew clean build

  popd >/dev/null
}

copy_release_artefact() {
  local source="$1"; shift
  local dest="$1"; shift
  local description="$1"; shift

  echo -e "${GREEN}Copying release artefact ${BLUE}${source}${NC}"

  mkdir -p "${RELEASE_ARTEFACTS_DIR}"

  cp "${source}" "${dest}"

  local filename
  if [[ -f "${dest}" ]]; then
    filename="$(basename "${dest}")"
  else
    filename="$(basename "${source}")"
  fi

  # Add an entry to a manifest file for the release artefacts
  echo "${filename} - ${description}" \
    >> "${RELEASE_MANIFEST}"
}

# Put all release artefacts in a dir to make it easier to upload them to
# Github releases. Some of them are needed by the stack builds in
# stroom-resources
gather_release_artefacts() {
  mkdir -p "${RELEASE_ARTEFACTS_DIR}"

  local -r libs_dir="${BUILD_DIR}/event-logging-api/build/libs"

  echo "Copying release artefacts to ${RELEASE_ARTEFACTS_DIR}"

  # The zip dist config is inside the zip dist. We need the docker dist
  # config so stroom-resources can use it.

  # Stroom
  copy_release_artefact \
    "${BUILD_DIR}/CHANGELOG.md" \
    "${RELEASE_ARTEFACTS_DIR}" \
    "Change log for this release"

  copy_release_artefact \
    "${libs_dir}/event-logging-schema-${MAVEN_VERSION}.jar" \
    "${RELEASE_ARTEFACTS_DIR}" \
    "Event logging library JAR"

  copy_release_artefact \
    "${libs_dir}/event-logging-schema-${MAVEN_VERSION}-sources.jar" \
    "${RELEASE_ARTEFACTS_DIR}" \
    "Sources JAR"

  copy_release_artefact \
    "${libs_dir}/event-logging-schema-${MAVEN_VERSION}-javadoc.jar" \
    "${RELEASE_ARTEFACTS_DIR}" \
    "Javadoc JAR"

  # Now generate hashes for all the zips
  for file in "${RELEASE_ARTEFACTS_DIR}"/*.jar; do
    create_file_hash "${file}"
  done
}

copy_gh_pages_content() {
  # Add a .nojekyll file to stop github assuming the gh-pages content contains
  # jekyll templates.
  touch "${GH_PAGES_DIR}/.nojekyll"

  mkdir -p "${GH_PAGES_DIR}"
  echo "Copying javadoc files to ${GH_PAGES_DIR}"
  # copy our generated javadoc to gh-pages
  cp \
    "${BUILD_DIR}"event-logging-api/build/docs/javadoc/* \
    "${GH_PAGES_DIR}/"
}

#establish what version we are building
EXTRA_BUILD_ARGS=()
if [ -n "$BUILD_TAG" ]; then
  #Tagged commit so use that as our version, e.g. v1.2.3
  PRODUCT_VERSION="${BUILD_TAG}"
  # Remove the leading 'v'
  MAVEN_VERSION="${BUILD_TAG#v}"

  # GPG sign the artifacts, publish to nexus then close and release
  # the staging repo to the public nexus repo and on to central
  # If you want to leave the published items in the staging repo
  # for manual checking then use closeAndSonatypeStagingRepository

  # For singing and publishing to work you will need to do:
  # Generate a GPG2 key
  # To display the secret key in base64 form
  # key="$(gpg2 --armor --export-secret-keys <key id> | base64 -w0)"; echo -e "-------\n$key\n-------"; key=""
  EXTRA_BUILD_ARGS=(
    "signMavenJavaPublication"
    "publishToSonatype"
    #"closeSonatypeStagingRepository"
    "closeAndReleaseSonatypeStagingRepository"
  )
else
  #No tag so use the branch name as the version, e.g. dev-SNAPSHOT
  #None tagged builds are NOT pushed to bintray
  PRODUCT_VERSION="SNAPSHOT"
  MAVEN_VERSION="SNAPSHOT"
  EXTRA_BUILD_ARGS=()
fi

#Dump all the travis env vars to the console for debugging
echo -e "PRODUCT_VERSION:               [${GREEN}${PRODUCT_VERSION}${NC}]"
echo -e "HOME:                          [${GREEN}${HOME}${NC}]"
echo -e "BUILD_DIR:                     [${GREEN}${BUILD_DIR}${NC}]"
echo -e "BUILD_COMMIT:                  [${GREEN}${BUILD_COMMIT}${NC}]"
echo -e "BUILD_BRANCH:                  [${GREEN}${BUILD_BRANCH}${NC}]"
echo -e "BUILD_TAG:                     [${GREEN}${BUILD_TAG}${NC}]"
echo -e "BUILD_IS_PULL_REQUEST:         [${GREEN}${BUILD_IS_PULL_REQUEST}${NC}]"
echo -e "BUILD_VERSION:                 [${GREEN}${BUILD_VERSION}${NC}]"
echo -e "MAVEN_VERSION:                 [${GREEN}${MAVEN_VERSION}${NC}]"
echo -e "LOCAL_BUILD:                   [${GREEN}${LOCAL_BUILD}${NC}]"
echo -e "docker version:                [${GREEN}$(docker --version)${NC}]"
echo -e "docker-compose version:        [${GREEN}$(docker-compose --version)${NC}]"
echo -e "git version:                   [${GREEN}$(git --version)${NC}]"
echo -e "java version:                  [${GREEN}$(java --version)${NC}]"

#Run the build (including running maven install task to generate poms
./gradlew -Pversion="${PRODUCT_VERSION}" clean build "${EXTRA_BUILD_ARGS[@]}"

ls -l "${BUILD_DIR}/event-logging-api/build/libs/"

# If it is a tagged build copy all the files needed for the github release
# artefacts
if [ -n "$BUILD_TAG" ]; then
  gather_release_artefacts
  copy_gh_pages_content
fi

exit 0

# vim:sw=2:ts=2:et:
