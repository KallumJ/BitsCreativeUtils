pipeline {
    agent any

    environment {
        DISCORD_WEBHOOK = credentials('discord-webhook')
    }

    tools {
        jdk 'jdk_17'
    }

    stages {
        stage('Build') {
            steps {
                sh "sed -i 's/%VERSION%/${BRANCH_NAME}-${BUILD_NUMBER}/' gradle.properties"
                sh './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                sh 'rm -rf test/'
                sh 'git clone https://hogwarts.bits.team/git/Bits/TestServer.git test/'
                sh 'chmod +x test/production_server_test.sh'
                sh """test/production_server_test.sh \
                        --java-path '${JAVA_HOME}' \
                        --mod-jar 'bits-creative-utils-${BRANCH_NAME}-${BUILD_NUMBER}.jar' \
                        --mc-version '1.19' \
                        --loader-version '0.14.6' \
                   """
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            discordSend link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: "$DISCORD_WEBHOOK"
        }
    }
}