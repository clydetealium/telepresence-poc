#!/bin/bash
set -eo pipefail
source jenkins/logging.sh

jfrog rt search \
  --user ${ARTIFACTORY_CREDENTIALS_USR} \
  --password=${ARTIFACTORY_CREDENTIALS_PSW} \
  --url=https://tealium.jfrog.io/artifactory \
  docker-virtual-registry/${COMPONENT}/${SOURCE_VERSION}/manifest.json | tee build_metadata.json