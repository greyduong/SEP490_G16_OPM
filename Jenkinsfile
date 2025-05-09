pipeline {
	agent any

	stages {
		stage("Build") {
			steps {
				sh "podman-compose build"
			}
		}
		stage("Down") {
			steps {
				sh "podman-compose down || true"
			}
		}
		stage("Up") {
			steps {
				sh "export JENKINS_NODE_COOKIE=dontKillMe && podman-compose up &"
			}
		}
	}
}
