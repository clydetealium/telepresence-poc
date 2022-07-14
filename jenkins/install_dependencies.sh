#!/usr/bin/env bash
set -eo pipefail
source ${WORKSPACE}/jenkins/logging.sh

log_messagec cyan "Cloning additional repos"
git config --global user.name "${GIT_CREDS_USR}"
git config --global user.email "${GIT_CREDS_USR}@tealium.com"
git config --global pull.rebase false
rm -rf ${LOCAL_MANIFESTS_REPO} || true
git clone https://${GIT_CREDS}@github.com/Tealium/${LOCAL_MANIFESTS_REPO}.git
rm -rf ${PROD_MANIFESTS_REPO} || true

log_messagec green "Script $0 completed • Success: true"