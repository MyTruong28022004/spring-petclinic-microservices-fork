pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Nhánh Git để xây dựng') // Thêm parameter cho nhánh Git
    }

    environment {
        REPO_URL = 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git'
        BRANCH_NAME = "${params.BRANCH_NAME}" // Sử dụng parameter BRANCH_NAME
        IMAGE_NAME = 'main'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout nhánh được chỉ định
                    git branch: "${BRANCH_NAME}", url: "${REPO_URL}"
                }
            }
        }

        stage('Get Latest Commit') {
            steps {
                script {
                    // Lấy hash commit mới nhất
                    LATEST_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Latest Commit Hash: ${LATEST_COMMIT}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image với commit hash làm tag
                    sh "docker build -f Dockerfile -t ${IMAGE_NAME}:${LATEST_COMMIT} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push Docker image lên registry (tùy chọn)
                    sh "docker push ${IMAGE_NAME}:${LATEST_COMMIT}"
                }
            }
        }
    }
}
