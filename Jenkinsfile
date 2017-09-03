/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

def findReleaseVersion = { ->
    def version = env.MAVEN_VERSION
    if (!version) {
        return ''
    } else {
        return version.replace('-SNAPSHOT', '')
    }
}

def findNextSnapshotVersion = { ->
    def releaseVersion = findReleaseVersion()
    def versionParts = releaseVersion.split('\\.')
    def lastPartIndex = versionParts.length - 1
    try {
        def lastPartInt = Integer.parseInt(versionParts[lastPartIndex])
        lastPartInt += 1;
        versionParts[lastPartIndex] = Integer.toString(lastPartInt)
        return versionParts.join('.') + '-SNAPSHOT'
    } catch (NumberFormatException ignored) {
        return ''
    }
}

pipeline {
    agent none // Don't block an agent while waiting for approval

    environment {
        MAVEN_VERSION = readMavenPom().getVersion()
    }

    parameters {
        booleanParam(defaultValue: false,
                description: 'Run Maven Release build?',
                name: 'doRelease')
        string(defaultValue: '%auto%',
                description: 'Release version',
                name: 'releaseVersion')
        string(defaultValue: '%auto%',
                description: 'Next development version',
                name: 'devVersion')
        booleanParam(defaultValue: false,
                description: 'Dry run only?',
                name: 'dryRun')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '50'))
        skipDefaultCheckout()
    }

    tools {
        maven 'Default Maven'
    }

    stages {

        stage('Maven Package') {
            agent any
            steps {
                deleteDir()
                checkout scm
                withMaven {
                    sh 'mvn -B package'
                }
                sh 'echo Package workspace: $(pwd)'
            }
        }

        stage('Generate Javadocs') {
            agent any
            steps {
                withMaven {
                    sh 'mvn -B javadoc:jar javadoc:aggregate'
                }
                sh 'echo Javadocs workspace: $(pwd)'
            }
        }

        stage('Compute Release Versions') {
            when {
                expression {
                    params.doRelease && (params.releaseVersion == '%auto%' || params.devVersion == '%auto%')
                }
            }
            agent any
            steps {
                script {
                    if (params.releaseVersion == '%auto%') {
                        params.releaseVersion = findReleaseVersion()
                        echo "Computed release version: ${params.releaseVersion}"
                    }
                    if (params.devVersion == '%auto%') {
                        params.devVersion = findNextSnapshotVersion()
                        echo "Computed dev version: ${params.devVersion}"
                    }
                }
                input """
                Do these computed versions look okay?
                Release version: ${params.releaseVersion}
                Development version: ${params.devVersion}
                """
            }
        }

        stage('Maven Release') {
            when { expression { params.doRelease } }
            agent any
            steps {
                echo 'Release version: ' + releaseVersion
                echo 'Dev version: ' + devVersion
                echo 'Dry run: ' + dryRun
            }
        }
    }
}