#!/usr/bin/env bash
set -eo pipefail
source ${WORKSPACE}/jenkins/logging.sh

# jansi.force only works when defined in MAVEN_OPTS, style.color only works when passed as an argument
export MAVEN_OPTS="-Djansi.force=true"
export COMMON_MVN_OPTIONS="--settings ../settings.xml --batch-mode --errors --no-transfer-progress --threads 4.0C -Dstyle.color=always"

mvn_versions_set() {
  pushd info
  log_messagec magenta "Running Command: mvn_versions_set On Branch: ${GIT_BRANCH}"
  if [ ${RUNNING_ON_DEFAULT_BRANCH} == 'true' ]; then
    RELEASE_VERSION=REL.$(date +%Y-%m-%d_%H%M)
    log_messagec magenta "Starting release build against release version=${RELEASE_VERSION}"
    mvn versions:set ${COMMON_MVN_OPTIONS} -DnewVersion=$RELEASE_VERSION
  else
    mvn versions:set ${COMMON_MVN_OPTIONS} -DnewVersion=${GIT_COMMIT}-SNAPSHOT -DgenerateBackupPoms=false &&
      log_messagec green "mvn_versions_set Complete" ||
      msg_and_exit 2 "mvn_versions_set Failed"
  fi
  popd
}

mvn_deploy() {
  log_messagec magenta "Running Command: mvn_deploy On Branch: ${GIT_BRANCH}"
  mvn deploy ${COMMON_MVN_OPTIONS} -Pnative -Dquarkus.native.additional-build-args='--initialize-at-run-time=javax.imageio.ImageTypeSpecifier\,com.sun.imageio.plugins.jpeg.JPEG\$JCS\,org.apache.http.impl.auth.NTLMEngineImpl,-H:ReflectionConfigurationFiles=reflect-config.json,--allow-incomplete-classpath, --report-unsupported-elements-at-runtime' &&
  log_messagec green "mvn_deploy Complete" ||
  msg_and_exit 3 "mvn_deploy Failed"
}

hub_release() {
  if [[ -n ${RELEASE_VERSION} ]]; then
    log_messagec magenta "Running Command: hub_release On Branch: ${GIT_BRANCH}"

    export GITHUB_USER=${GIT_CREDS_USR}
    export GITHUB_TOKEN=${GIT_CREDS_PSW}
    hub config url.https://${GIT_CREDS}@github.com/.insteadOf https://github.com/
    git config --global user.name "${GIT_CREDS_USR}"
    git config --global user.email "${GIT_CREDS_USR}@tealium.com"

    hub release create --message "${RELEASE_VERSION}" $RELEASE_VERSION
    log_messagec green "hub_release Complete"
  fi
}

build_em() {
  local project_subdirs=info,locality,personality
  for project_subdir in ${project_subdirs//,/ }
  do
    pushd ${WORKSPACE}/$project_subdir
    pwd
    if mvn_versions_set && mvn_deploy && hub_release; then
      log_messagec green "Build Complete"
    else
      RETURN_VALUE=$?
      msg_and_exit ${RETURN_VALUE} "Build Failed With Status: ${RETURN_VALUE}"
    fi
    popd
  done
}

echo "Initiating Build"
case ${ACTION} in
  BUILD_JAR)
    build_em
    ;;
  *) msg_and_exit 1 "Invalid Action, Exiting ..." ;;
esac