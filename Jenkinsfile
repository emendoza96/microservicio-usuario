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
                sh 'echo building develop'
            }
        }
        stage('backend tests') {
            steps {
                sh './mvnw test'
                sh 'echo running tests'
            }
        }
        stage('Install - Master') {
            steps {
                sh "./mvnw clean install site -DskipTests"
                sh './mvnw pmd:pmd'
                sh './mvnw pmd:cpd'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: '**/target/site/**'
            }
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
}
