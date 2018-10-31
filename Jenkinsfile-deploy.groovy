@Library("liferay-sdlc-jenkins-lib")

import org.liferay.sdlc.*
import org.liferay.sdlc.aws.*
import jenkins.model.*
import static org.liferay.sdlc.SDLCPrUtilities.*
import static org.liferay.sdlc.BuildUtils.*

properties([disableConcurrentBuilds(),
    [$class: 'ParametersDefinitionProperty',
        parameterDefinitions: [
            run(name:'MasterBuild'
                ,description:'Build Number'
                ,projectName:"oneclick-pr-builder/master"
                ,filter:'STABLE')
        ]
    ]
])

awsExecutor = new AwsCommand("aws-alpha-test")

//node ("aws-pool") {
node ("azure-infrastructure") {

    //hardcode
    def version = new Date().getTime()

    //def githubOrganization = "dsbfranco"
    def githubOrganization = "silvia-shimabuko"
    def githubProjectName = "oneclick"
    def githubCredentialsId = "github_silvia_oneclick"
    def inputJiraKey = "silviaxyz"
    def awsCredentialsId = "aws-alpha-test"

    def gitCommit = getCommit(env.MasterBuild_JOBNAME, env.MasterBuild_NUMBER, githubProjectName)
    def environment = "dev"
    def TAG = version
    def jiraKeyCustom = extractJiraKey(inputJiraKey)


    stage("Cleanup") {
        step([$class: 'WsCleanup'])
    }

    stage('Checkout') {
      checkout(
          [$class: 'GitSCM',
          //branches: [[ name: gitCommit ]],
          branches: [[ name: 'master']],
          doGenerateSubmoduleConfigurations: false,
          extensions: [
              [$class: 'CleanBeforeCheckout'],
              [$class: 'LocalBranch', localBranch: 'master'],
              [$class: 'IgnoreNotifyCommit']
            ],
            submoduleCfg: [],
            userRemoteConfigs: [
                [credentialsId: githubCredentialsId,
                url: "https://github.com/${githubOrganization}/${githubProjectName}.git"]]])

        TAG = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    }

    stage('Build') {
        gradlew "initBundle -Pliferay.workspace.environment=${environment}"
        gradlew "distBundleZip -Pliferay.workspace.environment=${environment}"
    }

    stage ('Copy configuration files') {

        dir('azure') {
            def fileConfig = "azure_silviaxy"
            withCredentials([file(credentialsId: fileConfig, variable: 'FILE')]) {
                sh "tar -xvzf $FILE"
            }
        }

        //sh "cp -r ./azure/* ."

        //sh "cp ./tmp_files/config/certs/cert.pem ./configs/certs/cert.pem"
        //sh "cp ./tmp_files/config/certs/key.pem ./configs/certs/key.pem"
        //sh "cp -r ./tmp_files/config ./config"
        //sh "cp -r ./tmp_files/terraform ./terraform"
        def DIST_ZIP = "${JOB_NAME}.zip"
        sh "cp -f ./build/$DIST_ZIP ./azure/docker/dxp/liferay-dxp/liferay-dxp.zip"
        if (fileExists("./configs/liferay/deploy/activation-key.xml")) {
            sh "cp -f ./configs/liferay/deploy/activation-key.xml ./azure/docker/dxp/liferay-dxp-customizations/license"
        } else {
            error "Activation Key not found on configs/liferay/deploy/activation-key.xml"
        }
    }

    stage('Create env dxp file'){
        dir ('azure/docker/dxp/liferay-dxp'){
            //create file
            def fileName = 'env'+jiraKeyCustom + '.sh'
            def properties = """
                readonly VERSION=$jiraKeyCustom
                readonly DXP_FILENAME="liferay-dxp.zip"
            """
            writeFile file: fileName, text: properties
            sh "cat ${fileName} | sed -e 's/^[ \t]*//' -i ${fileName}"
        }
    }

    /*
    stage ('Package Database') {
        pullDockerImages()
    }
    */

    stage('Build and Push') {
        dir('azure') {
            sh "cd scripts && ./build-docker-and-push-images.sh"
        }
    }

    stage('Build and Push') {
        dir('azure') {
            sh "cd scripts && ./setup-or-upgrade-portal.sh"
        }
    }

}

def pullDockerImages() {
    docker 'pull mysql/mysql-server:5.7'
}

def docker(String command) {
    sh script: "docker $command", returnStdout: true
}
/*
def createDeployPortalFile(awsRepositoryUri, tag, projectName) {
    def fileName = "deploy-liferay-portal.yml"

    writeFile file: fileName, text: updateTemplateVariables("deploy-liferay-portal.tpl", [
        STACK_NAME              : "ec2-$projectName",
        CLUSTER_NAME            : "cluster-$projectName",
        KEY_PAIR_NAME           : "sdlcservices",
        TASK_DEFINITION_NAME    : "task-$projectName",
        LIFERAY_PORTAL_NAME     : "service-$projectName",
        AWS_ECR_URL             : awsRepositoryUri,
        GITHUB_REPOSITORY_NAME  : projectName,
        TAG                     : tag
    ])
    fileName
}
*/
def updateTemplateVariables(templateName, varMap) {
    def txt = readFile file: templateName
    for (e in varMap) {
        txt = txt.replace("#{"+e.key+"}", e.value)
    }
    txt
}

def getCommit(jobName, buildNumber, projectName) {
  def shaCommit = ""
  def build = Jenkins.instance.getItemByFullName(jobName).getBuild(buildNumber)
  build.getActions(hudson.plugins.git.util.BuildData.class).each {
    if (it.remoteUrls.first().contains(projectName))
    	shaCommit = it.lastBuiltRevision.sha1String
  }
  shaCommit
}


def extractJiraKey(inputJiraKey){
    int maxSize = 8
    String jiraKey = inputJiraKey.replaceAll("[^A-Za-z]","").toLowerCase()
    return jiraKey.substring(0, ((jiraKey.length() < maxSize) ? jiraKey.length(): maxSize))
}