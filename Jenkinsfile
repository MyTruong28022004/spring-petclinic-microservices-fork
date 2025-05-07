pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mytruong28022004'
        CREDENTIALS_ID = 'github-token-1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: "*/**"]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/MyTruong28022004/spring-petclinic-microservices-fork.git',
                        credentialsId: "${env.CREDENTIALS_ID}"
                    ]]
                ])
            }
        }

        stage('Get Commit ID & Branch') {
            steps {
                script {
                    COMMIT_ID = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    BRANCH_NAME = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                    echo "Branch: ${BRANCH_NAME}"
                    echo "Commit ID: ${COMMIT_ID}"
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]
                    for (service in services) {
                        dir("${service}") {
                            def commitTag = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                            echo "Building image: ${commitTag}"
                            sh "docker build -t ${commitTag} ."

                            if (BRANCH_NAME == "main") {
                                def latestTag = "${DOCKERHUB_USERNAME}/${service}:latest"
                                sh "docker tag ${commitTag} ${latestTag}"
                            }
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    def services = [
                        'spring-petclinic-customers-service',
                        'spring-petclinic-vets-service',
                        'spring-petclinic-visits-service',
                        'spring-petclinic-genai-service'
                    ]
                    for (service in services) {
                        def commitTag = "${DOCKERHUB_USERNAME}/${service}:${COMMIT_ID}"
                        echo "Pushing image: ${commitTag}"
                        sh "docker push ${commitTag}"

                        if (BRANCH_NAME == "main") {
                            def latestTag = "${DOCKERHUB_USERNAME}/${service}:latest"
                            echo "Pushing image: ${latestTag}"
                            sh "docker push ${latestTag}"
                        }
                    }
                }
            }
        }
    }
}
