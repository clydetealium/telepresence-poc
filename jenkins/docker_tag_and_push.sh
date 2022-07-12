#!/bin/bash
set -eo pipefail
source jenkins/logging.sh

COMPONENT=$2
test -z "$1" && msg_and_exit 1 "must pass TAG as first argument" || log_messagec magenta "Building ${COMPONENT} image with tag $1"

apk add --update --no-cache curl zip
curl -fL https://getcli.jfrog.io | sh && chmod +x jfrog && mv jfrog /bin/jfrog

docker tag ${COMPONENT}:latest tealium-docker-virtual-registry.jfrog.io/${COMPONENT}:$1

jfrog rt docker-push \
  --user ${ARTIFACTORY_CREDENTIALS_USR} \
  --password=${ARTIFACTORY_CREDENTIALS_PSW} \
  --url=https://tealium.jfrog.io/artifactory \
  --build-name ${COMPONENT}-docker \
  --build-number ${SOURCE_VERSION} \
  tealium-docker-virtual-registry.jfrog.io/${COMPONENT}:$1 \
  docker-virtual-registry

jfrog rt build-publish \
  --user ${ARTIFACTORY_CREDENTIALS_USR} \
  --password=${ARTIFACTORY_CREDENTIALS_PSW} \
  --url https://tealium.jfrog.io/artifactory \
  ${COMPONENT}-docker \
  ${SOURCE_VERSION}

log_messagec green "Built, tagged and pushed up ${COMPONENT}:$1 • Success"

# # Config Templates
# zip -j ${COMPONENT}-configs.zip ./configuration-templates/*

# log_message "${COMPONENT}: push config templates"
# # Push configs templates
# jfrog rt u \
#   --user ${ARTIFACTORY_CREDENTIALS_USR} \
#   --password=${ARTIFACTORY_CREDENTIALS_PSW} \
#   --url=https://tealium.jfrog.io/artifactory \
#   --build-name ${COMPONENT}-configs \
#   --build-number ${SOURCE_VERSION} \
#   ${COMPONENT}-configs.zip \
#   generic-configs-virtual-registry/${COMPONENT}-${SOURCE_VERSION}-configs.zip

# log_message "${COMPONENT}: publish config templates"
# # Publish config templates
# jfrog rt build-publish \
#   --user ${ARTIFACTORY_CREDENTIALS_USR} \
#   --password=${ARTIFACTORY_CREDENTIALS_PSW} \
#   --url https://tealium.jfrog.io/artifactory \
#   ${COMPONENT}-configs \
#   ${SOURCE_VERSION}

# rm ${COMPONENT}-configs.zip

# log_messagec green "Pushed up ${COMPONENT}:$1 • config templates Success"