pipeline {
    agent any
    environment {
                NEXUS_CREDS = credentials('senexus-credential')
                DOCKER_PROXY_HOST = "senexus.pccth.com:8082"
                DOCKER_PRIVATE_HOST = "senexus.pccth.com:8083"
                REPLICACOUNT = "1"
                //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
                PROJECT_VERSION = readMavenPom().getVersion()
                PROJECT_NAME = readMavenPom().getArtifactId()            
    }
 
    stages {
        stage("Complie") {
            steps {
                sh "mvn -B -DskipTests clean install"
                //sh "mvn package"         
            }
        }

 
    }
}