
pipeline {
    agent any  // Use any available agent
environment {
APP_VERSION = ''
}
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'development', // Replace 'main' with your branch name
                    credentialsId: 'git-hub-credentials', // Replace with your Git credentials ID (if needed)
                    url: 'https://github.com/VFALAN/msc-ms-authentication.git' // Replace with your Git repository URL
            }
        }
        stage('Build Project') {
            steps {
                bat 'mvn clean package' // Adjust command for your build tool (e.g., Gradle: ./gradlew clean build)
            }
        }
        stage('Get Version') {
        steps {
        script {
        def appVersion = powershell(script: '''
        $content = Get-Content application.properties | Select-String -Pattern "msc.app.version="
        if($content){
        $content -split "=" | Select-Object -Last 1 | Out-String -Trim
        }else{
        ""
        }
        ''',returnStdout: true).trim()
        echo "app Version: ${appVersion}"
        env.APP_VERSION = appVersion
        }
        }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com','dcoker-credentials'){
                        def dockerImageBuild = docker.build("vifa951002/msc-ms-authentication:${APP_VERSION}")
                        dockerImageBuild.push()
                    }
                }

            }
        }
        /* stage('Deploy App'){
                steps{
               withCredentials(bindings: [
                                     string(credentialsId: 'k8s-token', variable: 'api_token')
                                     ]) {
                                     bat 'dir'
                           bat 'kubectl --token $api_token --server http://127.0.0. apply -f DeployK8s.yaml --validate=false'
                         }
        }


    } */
}
}