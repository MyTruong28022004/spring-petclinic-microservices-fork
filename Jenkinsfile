pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-cred') //Tạo trong Jenkins
        IMAGE_REPO = "mytruong28022004/main"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    COMMIT_ID = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                }
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_REPO:$COMMIT_ID .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh """
                  echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin
                  docker push $IMAGE_REPO:$COMMIT_ID
                """
            }
        }
    }
}
