pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mytruong28022004'
        DOCKERHUB_PASSWORD = credentials('docker-hub-cred')
        CREDENTIALS_ID = 'github-token-1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: "*/main"]],
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

        stage('Docker Login') {
            steps {
                sh 'echo "28102004Tm@" | docker login -u $DOCKERHUB_USERNAME --password-stdin'
            }
        }

        stage('Build & Push Docker Images') {
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
                            def image = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                            echo "Building and pushing image: ${image}"
                            sh """
                                docker build -t ${image} .
                                docker push ${image}
                            """
                        }
                    }
                }
            }
        }
    }
}
