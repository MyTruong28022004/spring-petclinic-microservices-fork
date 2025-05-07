pipeline {
  agent any
  environment {
    DOCKER_CREDENTIALS_ID = 'docker-hub-cred'  // Đảm bảo đây là ID đúng của credentials trên Jenkins
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
          // Lấy commit trước đó nếu không có HEAD^
          def prevCommit = sh(script: 'git rev-parse HEAD^ || git rev-parse HEAD', returnStdout: true).trim()
          
          // Lấy danh sách file thay đổi giữa commit hiện tại và commit trước đó
          def changedFiles = sh(
            script: "git diff --name-only ${prevCommit} HEAD",
            returnStdout: true
          ).trim().split("\n")
          
          // Danh sách các service có trong repo
          def services = [
            'spring-petclinic-customers-service',
            'spring-petclinic-vets-service',
            'spring-petclinic-visit-service',
            'spring-petclinic-genai-service'
          ]

          // Lọc các service có file thay đổi
          def changedServices = services.findAll { service -> 
            changedFiles.any { it.startsWith("${service}/") }
          }

          // In ra các service đã thay đổi
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
          def changedServices = env.CHANGED_SERVICES.split(',').findAll { it?.trim() }

          // Build và Push cho từng service đã thay đổi
          changedServices.each { service ->
            echo "Building and pushing image for: ${service}"

            // Xây dựng Docker image cho service
            sh """
              docker build -f Dockerfile \\
                --build-arg SERVICE=${service} \\
                -t ${IMAGE_NAME}/${service}:${commitId} \\
                -t ${IMAGE_NAME}/${service}:latest \\
                .
            """

            // Đăng nhập Docker Hub và đẩy image lên Docker Hub
            withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
              // Đăng nhập Docker Hub
              sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'

              // Đẩy image lên Docker Hub
              sh "docker push ${IMAGE_NAME}/${service}:${commitId}"
              sh "docker push ${IMAGE_NAME}/${service}:latest"
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
