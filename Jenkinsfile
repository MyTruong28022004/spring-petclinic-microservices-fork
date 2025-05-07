pipeline {
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: "${params.BRANCH_NAME}", url: 'https://github.com/spring-petclinic/spring-petclinic-microservices-fork.git'
            }
        }
        stage('Build & Push Docker Images') {
            steps {
                script {
                    def commitId = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
                    def services = ['spring-petclinic-customers-service', 'spring-petclinic-vets-service', 'spring-petclinic-visits-service', 'spring-petclinic-genai-service'] 
                    for (s in services) {
                        dir("${s}") {
                            sh "docker build -t mytruong28022004/${s}:${commitId} ."
                            sh "docker push mytruong28022004/${s}:${commitId}"
                        }
                    }
                }
            }
        }
    }
}
