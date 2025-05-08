pipeline {
	agent any

	stages {
		stage('Build Database') {
			steps {
				sh "podman build -t opm-sql ./Database/"
			}
		}
		stage('Build Tomcat') {
			steps {
				sh "podman build -t opm-web ./SEP490_G16_OPM/"
			}
		}
		stage('Stop Previous') {
			steps {
				sh "podman kill opm-web || true"
				sh "podman kill opm-sql || true"
				sh "podman rm opm-sql || true"
				sh "podman rm opm-web || true"
			}
		}
		stage('Deploy') {
			steps {
				sh "export JENKINS_NODE_COOKIE=dontKillMe && podman run -d --name opm-sql -p 1433:1433 --network=host opm-sql"
				sh "export JENKINS_NODE_COOKIE=dontKillMe && podman run -d --name opm-web -p 8080:8080 --network=host opm-web"
			}
		}
		stage('Prune') {
			steps {
				sh "podman system prune -f"
			}
		}
	}
}
