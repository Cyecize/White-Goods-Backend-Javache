pipeline {
    agent any

    environment {
        appVersion = 'tmp'
        imageVersion = 'tmp'
    }

    options {
            // Disallow concurrent executions of the Pipeline. Can be useful
            // for preventing simultaneous accesses to shared resources, etc.
            disableConcurrentBuilds()
            // Timestamp every output line
            timestamps()
    }

    stages {
        stage('Run on Docker') {
            steps {
                sh "/var/lib/jenkins/build-hhp.sh"
            }
        }
    }

    post {
    	always {
    		echo 'Clean workspace'
    		cleanWs()
    	}
    }
}
