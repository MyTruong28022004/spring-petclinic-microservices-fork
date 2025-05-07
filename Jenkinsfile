pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Nhánh Git để xây dựng')
    }

    environment {
        REPO_URL = 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git'
        IMAGE_NAME = 'mytruong28022004/spring-petclinic-microservices-fork' // Tên đầy đủ trên Docker Hub
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: "${params.BRANCH_NAME}", url: "${REPO_URL}"
                }
            }
        }

        stage('Get Latest Commit') {
            steps {
                script {
                    // Lấy hash commit mới nhất (ngắn)
                    COMMIT_ID = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Commit ID: ${COMMIT_ID}"
                    env.COMMIT_ID = COMMIT_ID
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build image với tag là commit id
                    sh "docker build -f Dockerfile -t ${IMAGE_NAME}:${COMMIT_ID} ."
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    // Login vào Docker Hub - cần cấu hình trước trong Jenkins Credentials (ID: docker-hub-cred)
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push image đã tag lên Docker Hub
                    sh "docker push ${IMAGE_NAME}:${COMMIT_ID}"
                }
            }
        }
    }
}
