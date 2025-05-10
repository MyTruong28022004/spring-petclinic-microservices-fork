pipeline {
  agent any
  environment {
    DOCKER_CREDENTIALS_ID = 'docker-hub-cred'
    REPO_URL = 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git'
    REPOSITORY_PREFIX = 'mytruong28022004' // dùng cho -Ddocker.image.prefix
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
          def prevCommit = sh(script: 'git rev-list --parents -n 1 HEAD | cut -d " " -f2 || git rev-parse HEAD', returnStdout: true).trim()
          def changedFiles = sh(script: "git diff --name-only ${prevCommit} HEAD", returnStdout: true).trim().split("\n")

          def services = [
            'spring-petclinic-customers-service',
            'spring-petclinic-vets-service',
            'spring-petclinic-visits-service',
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
        expression { return !env.CHANGED_SERVICES?.trim() }
      }
      steps {
        echo "No changed services detected. Skipping build and push."
      }
    }

    stage('Build & Push Changed Images') {
      when {
        expression { return env.CHANGED_SERVICES?.trim() }
      }
      steps {
        script {
          def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def changedServices = env.CHANGED_SERVICES.split(',').findAll { it?.trim() }

          def imageMap = [
            'spring-petclinic-customers-service': "${REPOSITORY_PREFIX}/spring-petclinic-customers-service",
            'spring-petclinic-vets-service'    : "${REPOSITORY_PREFIX}/spring-petclinic-vets-service",
            'spring-petclinic-visits-service' : "${REPOSITORY_PREFIX}/spring-petclinic-visits-service",
            'spring-petclinic-genai-service'  : "${REPOSITORY_PREFIX}/spring-petclinic-genai-service"
          ]

          withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
            sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'

            changedServices.each { service ->
              def imageName = imageMap[service]
              echo "Building and pushing image for: ${service} as ${imageName}"

              dir(service) {
                sh """
                  ../mvn clean install -Dmaven.test.skip=true \\
                    -P buildDocker \\
                    -Ddocker.image.prefix=${REPOSITORY_PREFIX} \\
                    -Dcontainer.build.extraarg="--push" \\
                    -Dcontainer.platform="linux/amd64,linux/arm64"
                """
                sh "docker tag ${imageName} ${imageName}:${commitId}"
                sh "docker push ${imageName}:${commitId}"
                sh "docker push ${imageName}:latest"
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
