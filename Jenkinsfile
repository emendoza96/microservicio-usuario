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
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
}