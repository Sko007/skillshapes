#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    gitlabCommitStatus('build') {
        stage('check java') {
            sh "java -version"
        }

        stage('quality analysis') {
            withSonarQubeEnv('sonar') {
            }
        }

        def dockerImage
        stage('publish docker') {
            // A pre-requisite to this step is to setup authentication to the docker registry
        }
    }
}
