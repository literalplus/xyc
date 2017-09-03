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

def findReleaseVersion = { org.apache.maven.model.Model model ->
    def version = model.getVersion()
    if (!version) {
        return ""
    } else {
        return version.replace('-SNAPSHOT', '')
    }
}

def findNextSnapshotVersion = { org.apache.maven.model.Model model ->
    def releaseVersion = findReleaseVersion(model)
    def versionParts = releaseVersion.split("\\.")
    def lastPartIndex = versionParts.length - 1
    try {
        def lastPartInt = Integer.parseInt(versionParts[lastPartIndex])
        lastPartInt += 1;
        versionParts[lastPartIndex] = String.valueOf(lastPartInt)
        return versionParts.join(".")
    } catch (NumberFormatException ignored) {
        return ""
    }
}

pipeline {
    agent none // Don't block an agent while waiting for approval

    options {
        buildDiscarder(logRotator(numToKeepStr: '50'))
        disableConcurrentBuilds()
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

        stage('Calculate Release Versions') {
            agent any
            steps {
                script {
                    project = readMavenPom
                    suggestedReleaseVersion = findReleaseVersion(project)
                    suggestedDevVersion = findNextSnapshotVersion(project)
                }
            }
        }

        stage('Release?') {
            steps {
                script {
                    releaseParams = input(
                            message: 'Release a new version?', submitter: 'xxyy',
                            parameters: [
                                    string(defaultValue: suggestedReleaseVersion,
                                            description: 'Release version',
                                            name: 'releaseVersion'),
                                    string(defaultValue: suggestedDevVersion,
                                            description: 'Next development version',
                                            name: 'devVersion'),
                                    booleanParam(defaultValue: false,
                                            description: 'Dry run only?',
                                            name: 'dryRun')
                            ]
                    )
                }
            }
        }

        stage('Maven Release') {
            agent any
            steps {
                echo 'Release version: ' + releaseParams['releaseVersion']
                echo 'Dev version: ' + releaseParams['devVersion']
                echo 'Dry run: ' + releaseParams['dryRun']
            }
        }
    }
}