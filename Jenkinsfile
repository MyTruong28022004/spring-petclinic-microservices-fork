pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-cred')
        IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("mytruong28022004/main:${IMAGE_TAG}", "./main")
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                script {
                    docker.withRegistry("https://index.docker.io/v1/", "docker-hub-cred") {
                        docker.image("mytruong28022004/main:${IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}
