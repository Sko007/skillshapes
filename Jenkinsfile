#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    gitlabCommitStatus('build') {
        docker.image('jhipster/jhipster:v6.10.5').inside('-u jhipster') {
            stage('check java') {
                sh "java -version"
            }

        }

        def dockerImage
        stage('publish docker') {
            // A pre-requisite to this step is to setup authentication to the docker registry
        }
    }
}