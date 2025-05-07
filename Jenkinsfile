pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mytruong28'
        DOCKERHUB_PASSWORD = credentials('docker-hub-cred') // DockerHub credentials trong Jenkins
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

        stage('Login DockerHub') {
            steps {
                sh '''
echo "Username: $DOCKERHUB_USERNAME"
echo "Password: $DOCKERHUB_PASSWORD"
'''

                sh 'echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin'
            }
        }

        stage('Build & Push Images with Buildx') {
            steps {
                script {
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]

                    sh 'docker buildx create --use || true'  // Tạo buildx nếu chưa có

                    for (service in services) {
                        dir("${service}") {
                            def image = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                            echo "Building and pushing: ${image}"
                            sh "docker buildx build --platform linux/amd64 -t ${image} --push ."
                        }
                    }
                }
            }
        }
    }
}
