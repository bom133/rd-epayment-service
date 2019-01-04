pipeline {
    agent any
    environment {
                NEXUS_CREDS = credentials('nexustoon-credential')
                DOCKER_PROXY_HOST = "nexustoon.com:8082"
                DOCKER_PRIVATE_HOST = "nexustoon.com:8083"
                REPLICACOUNT = "1"
                //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
                PROJECT_VERSION = readMavenPom().getVersion()
                PROJECT_NAME = readMavenPom().getArtifactId()            
    }
    stages {
        stage("Complie") {
            steps {
                sh "mvn -B -DskipTests clean install"       
            }
        }
       stage("Docker build") {
            steps {
                sh "printenv"
                //sh "docker login -u username  -p password senexus.pccth.com:8082"
                //sh "docker build --build-arg program_name=rd-epayment-service -t senexus.pccth.com:8083/rd-epayment-service:0.1.1-SNAPSHOT ."
                sh "docker login -u $NEXUS_CREDS_USR  -p $NEXUS_CREDS_PSW $DOCKER_PROXY_HOST"
                //sh "docker build --build-arg program_name=${PROJECT_NAME} -t ${DOCKER_PRIVATE_HOST}/${PROJECT_NAME}:${PROJECT_VERSION} ."
                sh "docker build --build-arg program_name=${PROJECT_NAME} -t ${DOCKER_PRIVATE_HOST}/${PROJECT_NAME}:${PROJECT_VERSION} ."
            }
        }
        stage("Docker push") {
            steps {
                //sh "docker login -u username  -p password senexus.pccth.com:8083"
                //sh "docker push senexus.pccth.com:8083/rd-epayment-service:0.1.1-SNAPSHOT"
                sh "docker login -u $NEXUS_CREDS_USR  -p $NEXUS_CREDS_PSW $DOCKER_PRIVATE_HOST"
                sh "docker push ${DOCKER_PRIVATE_HOST}/${PROJECT_NAME}:${PROJECT_VERSION}"
                //sh "docker push nexustoon.com:8083/rd-epayment-service:0.1.3-SNAPSHOT"
            }
        }
    }    
            
        
    
}