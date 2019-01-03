pipeline {
    agent any
 
    stages {
        stage("Complie") {
            steps {
                //sh "mvn -B -DskipTests clean install"
                sh "mvn package"         
            }
        }

 
    }
}