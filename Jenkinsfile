library(
    identifier: 'jenkins-shared-lib@SDLC-2445_telepresence_poc',
    retriever: modernSCM(
        [
            $class       : 'GitSCMSource',
            remote       : 'git@github.com:Tealium/jenkins-shared-lib.git',
            credentialsId: 'github-cicd-bot-teal'
        ]
    )
)

String build = 'build'
String webhooks = 'webhooks'
String imgBuildAndUpload = 'image-build-and-upload'
def sv

pipeline {
    agent {
        kubernetes {
            yamlFile 'jenkins/k8s-pods.yml'
        }
    }

    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '15'))
        skipStagesAfterUnstable()
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }

    environment {
        ARTIFACTORY_CREDENTIALS = credentials('artifactory-credentials-jenkins')
        COMPONENT_PREFIX = 'telepresence_poc'
        LOCAL_MANIFESTS_REPO = "kubernetes-environments-${env.ACCOUNT_NAME}"
        GIT_CREDS = credentials('github-cicd-bot-teal-token')
        ENVIRONMENT = "${env.ENVIRONMENT}"
        REGION = "${env.REGION}"
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Git Env') {
            steps {
                container(webhooks) {
                    script {
                        sv = sourceVersion('clydetealium')
                    }
                }
            }
        }

        stage('Install dependencies') {
            steps {
                container(build) {
                    sh './jenkins/install_dependencies.sh'
                }
                container(imgBuildAndUpload) {
                    sh '''
                        apk add --no-cache bash curl zip
                        curl -fL https://getcli.jfrog.io | sh && chmod +x jfrog && mv jfrog /bin/jfrog
                      '''
                }
            }
        }

        stage('Build and push docker image') {
            environment {
                GIT_BRANCH = "${sourceVersion.currentBranch()}"
                GIT_COMMIT = "${sourceVersion.gitsha()}"
                GIT_COMMIT_SHA = "${sourceVersion.gitsha()}"
                RUNNING_ON_DEFAULT_BRANCH = "${sourceVersion.runningOnDefaultBranch()}"
                SOURCE_VERSION = "${sourceVersion.gitsha()}"
            }

            stages {
                stage('Build Jar') {
                    steps {
                        container(build) {
                            sh 'env'
                            sh "ACTION=BUILD_JAR ./jenkins/build.sh"
                        }
                    }
                }

                stage('Docker build') {
                    steps {
                        container(imgBuildAndUpload) {
                            sh '''
                                project_subdirs=info,locality,personality
                                for project_subdir in ${project_subdirs//,/ }
                                do
                                    cd $project_subdir
                                    echo -n "$ARTIFACTORY_CREDENTIALS_PSW" | \
                                      docker login \
                                        --username "$ARTIFACTORY_CREDENTIALS_USR" \
                                        --password-stdin tealium-docker-virtual-registry.jfrog.io

                                    docker build --tag $COMPONENT_PREFIX_$project_subdir .
                                    cd ..
                                done
                              '''
                        }
                    }
                }

                stage('Docker push latest to JFrog') {
                    when { expression { env.RUNNING_ON_DEFAULT_BRANCH } }
                    steps {
                        container(imgBuildAndUpload) {
                            sh '''
                            project_subdirs=info,locality,personality
                            for project_subdir in ${project_subdirs//,/ }
                            do
                                jenkins/docker_tag_and_push.sh latest $COMPONENT_PREFIX_$project_subdir
                            done
                            '''
                        }
                    }
                }
            }
        }

        // stage('Update manifest in local kubernetes-environments repo') {
        //     environment {
        //         ACTION = 'update_gitsha_for_current_environment'
        //         SOURCE_VERSION = "${sourceVersion.gitsha()}"
        //     }
        //     steps {
        //         container(build) {
        //             sh "${env.LOCAL_MANIFESTS_REPO}/update_manifests.sh"
        //         }
        //     }
        // }
    }
}