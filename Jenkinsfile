pipeline {
  agent any
  environment {
    DOCKER_CREDENTIALS_ID = 'docker-hub-cred'
    REPO_URL = 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git'
    IMAGE_NAME = 'mytruong28022004/spring-petclinic-microservices-fork'
  }
  parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: "${params.BRANCH_NAME}", url: "${REPO_URL}"
      }
    }

    stage('Detect Changed Services') {
      steps {
        script {
          def changedFiles = sh(
            script: 'git diff --name-only $(git rev-list -n 1 HEAD^) HEAD || true',
            returnStdout: true
          ).trim().split("\n")
          
          def services = [
            'spring-petclinic-customers-service',
            'spring-petclinic-vets-service',
            'spring-petclinic-visit-service',
            'spring-petclinic-genai-service'
          ]

          def changedServices = services.findAll { service ->
            changedFiles.any { it.startsWith("${service}/") }
          }

          echo "Changed services: ${changedServices}"
          env.CHANGED_SERVICES = changedServices.join(',')
        }
      }
    }

    stage('Skip Build (No Services Changed)') {
      when {
        not {
          expression { return env.CHANGED_SERVICES?.trim() }
        }
      }
      steps {
        echo "No changed services detected. Skipping build and push."
      }
    }

    stage('Build & Push Changed Services') {
      when {
        expression { return env.CHANGED_SERVICES?.trim() }
      }
      steps {
        script {
          def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def changedServices = env.CHANGED_SERVICES.split(',')

          for (service in changedServices) {
            echo "Building and pushing image for: ${service}"
            dir(service) {
              sh "docker build -t ${IMAGE_NAME}/${service}:${commitId} ."
              sh "docker tag ${IMAGE_NAME}/${service}:${commitId} ${IMAGE_NAME}/${service}:latest"
              withDockerRegistry(credentialsId: "${DOCKER_CREDENTIALS_ID}") {
                sh "docker push ${IMAGE_NAME}/${service}:${commitId}"
                sh "docker push ${IMAGE_NAME}/${service}:latest"
              }
            }
          }
        }
      }
    }
  }

  post {
    always {
      echo "Cleaning up Docker images..."
      sh 'docker system prune -f || true'
    }
  }
}
