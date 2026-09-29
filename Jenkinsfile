pipeline {
    agent any

    environment {
        REGISTRY = "localhost:5000"
        REGISTRY_CREDENTIALS = credentials('private-registry-creds')
        BACKEND_IMAGE = "backend-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SamiBenAbdelkader/DevOps-AppGestionDesProjets.git'
            }
        }

        stage('Build & Test Maven') {
            steps {
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('backend') {
                    sh "docker build -t ${BACKEND_IMAGE}:latest -t ${REGISTRY}/${BACKEND_IMAGE}:latest ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                sh "echo ${REGISTRY_CREDENTIALS_PSW} | docker login ${REGISTRY} -u ${REGISTRY_CREDENTIALS_USR} --password-stdin"
                sh "docker push ${REGISTRY}/${BACKEND_IMAGE}:latest"
            }
        }

        stage('Déploiement MySQL') {
            steps {
                sh '''
                    if [ "$(docker ps -aq -f name=^mysql$)" ]; then
                        docker rm -f mysql
                    fi
                    docker run -d --name mysql \
                        -e MYSQL_ROOT_PASSWORD=root \
                        -e MYSQL_DATABASE=test_db \
                        -p 3306:3306 \
                        mysql:8.0
                '''
                sh 'sleep 20'
            }
        }

        stage('Déploiement backend-app') {
            steps {
                sh '''
                    if [ "$(docker ps -aq -f name=^backend-app$)" ]; then
                        docker stop backend-app || true
                        docker rm backend-app || true
                    fi
                    docker pull ${REGISTRY}/${BACKEND_IMAGE}:latest
                    docker run -d --name backend-app \
                        --link mysql:mysql \
                        -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/test_db \
                        -p 8090:8080 \
                        ${REGISTRY}/${BACKEND_IMAGE}:latest
                '''
                sh 'sleep 15'
            }
        }

        stage('Vérification du déploiement') {
            steps {
                sh 'docker ps'
                sh 'docker logs backend-app'
            }
        }
    }

    post {
        always {
            sh 'docker logout ${REGISTRY} || true'
        }
        success {
            echo 'Déploiement CD terminé avec succès !'
        }
        failure {
            echo 'Le pipeline CD a échoué.'
        }
    }
}
