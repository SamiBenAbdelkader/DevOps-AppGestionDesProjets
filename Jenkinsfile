pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        DOCKERHUB_USER = 'samibenabdelkader'
        BACKEND_IMAGE = "${DOCKERHUB_USER}/timesheet-backend"
        FRONTEND_IMAGE = "${DOCKERHUB_USER}/timesheet-frontend"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SamiBenAbdelkader/DevOps-AppGestionDesProjets.git'
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh 'docker rm -f mysql-test-ci || true'
                    sh 'docker run -d --name mysql-test-ci -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test_db -p 3307:3306 mysql:8.0'
                    sh 'sleep 20'
                    sh 'SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/test_db ./mvnw test'
                }
            }
            post {
                always {
                    sh 'docker rm -f mysql-test-ci || true'
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                dir('backend') {
                    sh "docker build -t ${BACKEND_IMAGE}:${BUILD_NUMBER} -t ${BACKEND_IMAGE}:latest ."
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('frontend') {
                    sh "docker build -t ${FRONTEND_IMAGE}:${BUILD_NUMBER} -t ${FRONTEND_IMAGE}:latest ."
                }
            }
        }

        stage('Login to DockerHub') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        stage('Push Backend Image') {
            steps {
                sh "docker push ${BACKEND_IMAGE}:${BUILD_NUMBER}"
                sh "docker push ${BACKEND_IMAGE}:latest"
            }
        }

        stage('Push Frontend Image') {
            steps {
                sh "docker push ${FRONTEND_IMAGE}:${BUILD_NUMBER}"
                sh "docker push ${FRONTEND_IMAGE}:latest"
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
        success {
            echo 'Tests passés, images buildées et pushées avec succès !'
        }
        failure {
            echo 'Le pipeline a échoué.'
        }
    }
}
