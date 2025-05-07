pipeline {
    agent any

    environment {
        DOCKERHUB_NAMESPACE = "mytruong28022004"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout code từ nhánh hiện tại của repo
                    def branchName = sh(returnStdout: true, script: "git rev-parse --abbrev-ref HEAD").trim()
                    echo "Building branch: ${branchName}"
                    checkout scm
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    // Lấy commit ID cuối cùng
                    def commitId = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()

                    // Xác định tag dựa vào nhánh hiện tại
                    def branchName = sh(returnStdout: true, script: "git rev-parse --abbrev-ref HEAD").trim()
                    def tag = (branchName == "main") ? "latest" : commitId
                    
                    // Các dịch vụ cần build
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]

                    // Build và push cho mỗi service
                    for (s in services) {
                        dir("${s}") {
                            echo "Building ${s} with tag ${tag}"
                            sh "docker build -t ${DOCKERHUB_NAMESPACE}/${s}:${tag} ."
                            sh "docker push ${DOCKERHUB_NAMESPACE}/${s}:${tag}"
                        }
                    }
                }
            }
        }
    }
}
