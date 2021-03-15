pipeline {
    agent any
    stages {
        stage('Installs and Configs'){
            steps{
            sh "chmod -R 777 /var/run/docker.sock"
            sh "curl -fsSL https://get.docker.com -o get-docker.sh"
            sh "sh get-docker.sh"
            sh "usermod -aG docker root"
            sh "usermod -aG docker jenkins"
            sh "docker ps"
            sh "chmod -R 777 /etc/docker/"
            sh "touch /etc/docker/daemon.json"
            sh "chmod -R 777 /etc/docker/"
            sh "echo \"{ 'insecure-registries' : ['10.99.70.79:5000'] }\" > /etc/docker/daemon.json"
            }
        }
        stage('Git Checkout'){
            steps{
            //git branch: 'master', credentialsId: 'DM', url: 'https://gitlab.devoteam.de/AB05105/skillshapes.git'
            //checkout scm
            sh "chmod -R 777 *"
            }
        }
        stage('Build') {
            parallel{
                stage('microservice') {
                    steps{
                        sh 'cd microservice && ./mvnw -Pprod clean package -DskipTests'
                    }
                }
                
                stage('gateway') {
                    steps{
                         sh 'cd gateway && ./mvnw package -Pprod verify jib:dockerBuild -DskipTests'     
                    }
                }
            }
        }
        /*stage('Test') {
            parallel{
                stage('microservice'){
                    steps{
                        sh 'cd microservice && ./mvnw clean test'
                    }
                }
                stage('gateway'){
                    steps{
                        sh 'cd gateway && ./mvnw clean test'
                    }
                }
            }
                
        }*/
        stage('Deployment') {
            steps {
                sh 'echo "To Be Done"'
            }
        }
    }
}