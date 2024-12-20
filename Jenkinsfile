pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'Maven' // Ensure 'Maven' is correctly configured in Jenkins
    }
    stages {
        stage('Checkout') {
            steps {
                // Checkout with branch specified and credentials added
                git branch: 'main',
                    url: 'https://github.com/AyoubAmqicher/gestion-bibliotheque.git'            }
        }
        stage('Build') {
            steps {
                script {
                    // Use the appropriate command for Windows
                    if (isUnix()) {
                        sh '${MAVEN_HOME}/bin/mvn clean compile'
                    } else {
                        bat "${MAVEN_HOME}\\bin\\mvn clean compile"
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh '${MAVEN_HOME}/bin/mvn test'
                    } else {
                        bat "${MAVEN_HOME}\\bin\\mvn test"
                    }
                }
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        if (isUnix()) {
                            sh '${MAVEN_HOME}/bin/mvn sonar:sonar'
                        } else {
                            bat "${MAVEN_HOME}\\bin\\mvn sonar:sonar"
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Déploiement simulé réussi'
            }
        }
    }
    post {
        success {
            emailext to: 'ayoubamqicher@gmail.com',
                subject: 'Build Success',
                body: 'Le build a été complété avec succès.'
        }
        failure {
            emailext to: 'ayoubamqicher@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.'
        }
    }
}
