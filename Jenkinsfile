pipeline {
    agent any

    environment {
        DOCKERHUB_NAMESPACE = "mytruong28022004"
        DOCKER_COMPOSE_FILE = "docker-compose.yaml"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    def branchName = sh(returnStdout: true, script: "git rev-parse --abbrev-ref HEAD").trim()
                    echo "Checking out branch: ${branchName}"
                    checkout scm
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    def commitId = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
                    def branchName = sh(returnStdout: true, script: "git rev-parse --abbrev-ref HEAD").trim()
                    def tag = (branchName == "main") ? "latest" : commitId

                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]

                    for (s in services) {
                        echo "Skipping build for ${s} because no Dockerfile is present"
                        // Nếu sau này có Dockerfile thì bạn có thể bật lại đoạn dưới:
                        // dir("${s}") {
                        //     echo "Building ${s} with tag ${tag}"
                        //     sh "docker build -t ${DOCKERHUB_NAMESPACE}/${s}:${tag} ."
                        //     sh "docker push ${DOCKERHUB_NAMESPACE}/${s}:${tag}"
                        // }
                    }
                }
            }
        }

        stage('Deploy Using Docker Compose') {
            steps {
                script {
                    echo "Pulling latest images from Docker Hub..."
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} pull"

                    echo "Stopping old containers (if any)..."
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} down"

                    echo "Starting new containers..."
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} up -d"
                }
            }
        }
    }
}
