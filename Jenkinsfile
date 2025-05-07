pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
    }

    environment {
        DOCKERHUB_USERNAME = 'mytruong28022004'
        CREDENTIALS_ID = 'github-token-1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: "${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git',
                        credentialsId: "${env.CREDENTIALS_ID}"
                    ]]
                ])
            }
        }

        stage('Get Commit ID') {
            steps {
                script {
                    COMMIT_ID = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    echo "Commit ID: ${COMMIT_ID}"
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]
                    for (service in services) {
                        dir("${service}") {
                            def imageName = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                            echo "Building image ${imageName}"
                            sh "docker build -t ${imageName} ."
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]
                    for (service in services) {
                        def imageName = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                        echo "Pushing image ${imageName}"
                        sh "docker push ${imageName}"
                    }
                }
            }
        }
    }
}
