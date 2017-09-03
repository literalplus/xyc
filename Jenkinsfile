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

def findReleaseVersion = { String mavenVersion ->
    if (!mavenVersion) {
        return ''
    } else {
        return mavenVersion.replace('-SNAPSHOT', '')
    }
}

def findNextSnapshotVersion = { String mavenVersion ->
    def releaseVersion = findReleaseVersion(mavenVersion)
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

    parameters {
        booleanParam(defaultValue: false,
                description: 'Run Maven Release build?',
                name: 'doRelease')
        string(defaultValue: '%auto%',
                description: 'Release version',
                name: 'paramReleaseVersion')
        string(defaultValue: '%auto%',
                description: 'Next development version',
                name: 'paramDevVersion')
        booleanParam(defaultValue: false,
                description: 'Dry run only?',
                name: 'dryRun')
        booleanParam(defaultValue: false,
                description: 'Skip manual sanity check of generated versions (danger!)',
                name: 'skipVersionSanityCheck')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '50'))
        skipDefaultCheckout()
    }

    tools {
        maven 'Default Maven'
        git 'Default git'
    }

    stages {
        stage('Maven Package') {
            when {
                expression {
                    !params.doRelease
                }
            }
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
                publishHTML(
                        reportDir: 'target/site/apidocs', reportName: 'Javadocs', reportFiles: 'index.html',
                        keepAll: false, allowMissing: false, alwaysLinkToLastBuild: false,
                        reportTitles: 'Javadocs'
                )
            }
        }

        stage('Compute Release Versions') {
            when {
                expression {
                    params.doRelease
                }
            }
            agent any
            steps {
                script {
                    def mavenVersion = readMavenPom().getVersion()
                    if (params.paramReleaseVersion.equals('%auto%')) {
                        env.releaseVersion = findReleaseVersion(mavenVersion)
                    } else {
                        env.releaseVersion = params.paramReleaseVersion
                    }
                    if (params.paramDevVersion.equals('%auto%')) {
                        env.devVersion = findNextSnapshotVersion(mavenVersion)
                    } else {
                        env.devVersion = params.paramDevVersion
                    }
                    if (!params.skipVersionSanityCheck) {
                        input """
                              Do these computed versions look okay?
                              Release version: ${env.releaseVersion}
                              Development version: ${env.devVersion}
                              """
                    }
                }
            }
        }

        stage('Maven Release') {
            when { expression { params.doRelease } }
            agent any
            steps {
                withMaven {
                    script {
                        mvnParams = "-B -Dresume=false -DdryRun=${params.dryRun} " +
                                "-DdevelopmentVersion=${env.devVersion} -DreleaseVersion=${env.releaseVersion}"
                    }
                    echo "Preparing ${env.releaseVersion}..."
                    sh "mvn ${mvnParams} release:prepare"
                    echo "Releasing ${env.releaseVersion}..."
                    sh "mvn ${mvnParams} javadoc:jar release:perform"
                }
            }
        }
    }
}