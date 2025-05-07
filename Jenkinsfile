pipeline {
  agent any
  environment {
    DOCKER_CREDENTIALS_ID = 'docker-hub-cred'
    DOCKERHUB_NAMESPACE = 'mytruong2022004'
  }
  parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: "${params.BRANCH_NAME}", url: 'https://github.com/spring-petclinic/spring-petclinic-microservices-fork.git'
      }
    }

    stage('Detect Changed Services') {
      steps {
        script {
          def changedFiles = sh(script: 'git diff --name-only HEAD~1 HEAD', returnStdout: true).trim().split("\n")
          def services = ['customers-service', 'vets-service', 'visit-service', 'api-gateway', 'genai-service']

          // Lưu danh sách service thay đổi vào biến môi trường
          def changedServices = services.findAll { service ->
            changedFiles.any { it.startsWith("${service}/") }
          }
          echo "Changed services: ${changedServices}"

          // Lưu dưới dạng chuỗi để truyền qua stage sau
          env.CHANGED_SERVICES = changedServices.join(',')
        }
      }
    }

    stage('Build & Push Changed Services') {
      when {
        expression { return env.CHANGED_SERVICES }
      }
      steps {
        script {
          def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def changedServices = env.CHANGED_SERVICES.split(',')

          for (service in changedServices) {
            echo "Building and pushing image for: ${service}"
            dir(service) {
              sh "docker build -t $DOCKERHUB_NAMESPACE/${service}:${commitId} ."
              sh "docker tag $DOCKERHUB_NAMESPACE/${service}:${commitId} $DOCKERHUB_NAMESPACE/${service}:latest"
              withDockerRegistry(credentialsId: "${DOCKER_CREDENTIALS_ID}") {
                sh "docker push $DOCKERHUB_NAMESPACE/${service}:${commitId}"
                sh "docker push $DOCKERHUB_NAMESPACE/${service}:latest"
              }
            }
          }
        }
      }
    }
  }
}
