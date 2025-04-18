pipeline {
	agent any

	stages {
		stage('Build Database') {
			steps {
				sh "docker build -t opm-sql ./Database/"
			}
		}
		stage('Build Tomcat') {
			steps {
				sh "docker build -t opm-web ./SEP490_G16_OPM/"
			}
		}
		stage('Stop Previous') {
			steps {
				sh "docker kill opm-web || true"
				sh "docker kill opm-sql || true"
				sh "docker rm opm-sql || true"
				sh "docker rm opm-web || true"
			}
		}
		stage('Deploy') {
			steps {
				sh "docker run -d --name opm-sql -p 1433:1433 --network=host opm-sql"
				sh "docker run -d --name opm-web -p 8080:8080 --network=host opm-web"
			}
		}
		stage('Prune') {
			steps {
				sh "docker system prune -f"
			}
		}
	}
}
