#!/usr/bin/env groovy

pipeline {
    agent any
    stages {
        stage('clean') {
            when {
                branch 'main'
            }
            steps {
                sh 'java -version'
                sh './mvnw clean'
            }
        }
        stage('clean-develop') {
            when {
                branch 'develop'
            }
            steps {
                sh 'java -version'
                sh './mvnw clean'
                sh 'echo buildeando develop'
            }
        }
        stage('backend tests') {
            steps {
                sh './mvnw verify'
                sh 'echo "configurar para ejecutar los tests"'
            }
        }
        stage('Install - Master') {
            steps {
                sh "./mvnw clean install site -DskipTests"
                sh './mvnw pmd:pmd'
                sh './mvnw pmd:cpd'
            }
        }
    }
    post {
        success {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
        always {
            archiveArtifacts artifacts: '**/target/site/**', fingerprint: true
            publishHTML(allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site',
                        reportFiles: 'index.html',
                        reportName: 'Site')
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
            jacoco(execPattern: 'target/jacoco.exec')
            recordIssues enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc()]
            recordIssues enabledForFailure: true, tools: [checkStyle()]
            recordIssues enabledForFailure: true, tools: [spotBugs()]
            recordIssues enabledForFailure: true, tools: [cpd(pattern: '**/target/cpd.xml')]
            recordIssues enabledForFailure: true, tools: [pmdParser(pattern: '**/target/pmd.xml')]
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
}