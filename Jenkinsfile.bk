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
            }
        }
       stage("Docker build") {
            steps {
                sh "printenv"
                //sh "docker login -u username  -p password senexus.pccth.com:8082"
                //sh "docker build --build-arg program_name=rd-epayment-service -t senexus.pccth.com:8083/rd-epayment-service:0.1.1-SNAPSHOT ."
                sh "docker login -u $NEXUS_CREDS_USR  -p $NEXUS_CREDS_PSW $DOCKER_PROXY_HOST"
                sh "docker build --build-arg program_name=${PROJECT_NAME} -t ${DOCKER_PRIVATE_HOST}/${PROJECT_NAME}:${PROJECT_VERSION} ."
            }
        }
        stage("Docker push") {
            steps {
                //sh "docker login -u username  -p password senexus.pccth.com:8083"
                //sh "docker push senexus.pccth.com:8083/rd-epayment-service:0.1.1-SNAPSHOT"
                sh "docker login -u $NEXUS_CREDS_USR  -p $NEXUS_CREDS_PSW $DOCKER_PRIVATE_HOST"
                sh "docker push ${DOCKER_PRIVATE_HOST}/${PROJECT_NAME}:${PROJECT_VERSION}"
            }
        }
        
        stage("Deploy to kubernetes") {
            steps {
                //script must be approved
                //new java.io.File java.lang.String
                //staticMethod org.codehaus.groovy.runtime.DefaultGroovyMethods getText java.io.File
                //staticMethod org.codehaus.groovy.runtime.DefaultGroovyMethods write java.io.File java.lang.String
                //before using this script
                //script{
                //    def chartFile = new File("${WORKSPACE}/deployment/Chart.yaml")
                //    def chartText = chartFile.text
                //    appVersionReplaceStr = "appVersion: ${PROJECT_VERSION}"
                //    nameReplaceStr = "name: ${PROJECT_NAME}"
                //    versionReplaceStr = "version: ${PROJECT_VERSION}"
                //    chartText = chartText.replace("appVersion: dummy",appVersionReplaceStr)
                //    chartText = chartText.replace("name: dummy",nameReplaceStr)
                //    chartText = chartText.replace("version: dummy",versionReplaceStr)
                //    chartFile.write(chartText)
                //}
                sh """
                    perl -pi -e "s/appVersion:.*/appVersion: ${PROJECT_VERSION}/" ./deployment/Chart.yaml
                    perl -pi -e "s/name:.*/name: ${PROJECT_NAME}/" ./deployment/Chart.yaml
                    perl -pi -e "s/version:.*/version: ${PROJECT_VERSION}/" ./deployment/Chart.yaml
                    helm ls
                    helm upgrade --install --debug ${PROJECT_NAME} ./deployment \
                    --set image.tag=${PROJECT_VERSION} \
                    --set image.repository=${DOCKER_PRIVATE_HOST}/${PROJECT_NAME} \
                    --set replicaCount=${REPLICACOUNT} \
                    --set healthcheck.enabled=false \
                    --set healthcheck.livenessprobe=/${PROJECT_NAME}/swagger-ui.html \
                    --set healthcheck.readynessprobe=/${PROJECT_NAME}/swagger-ui.html \
                    --wait --timeout 180
                """
            }
        }
    }
}