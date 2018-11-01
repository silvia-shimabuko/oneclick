    @Library("liferay-sdlc-jenkins-lib")

    import org.liferay.sdlc.*
    import org.liferay.sdlc.aws.*
    import jenkins.model.*
    import static org.liferay.sdlc.SDLCPrUtilities.*
    import static org.liferay.sdlc.BuildUtils.*

    import groovy.transform.Field
    @Field final gitRepository = '#{_GITHUB_ORGANIZATION_}/#{_GITHUB_REPOSITORY_NAME_}'
    @Field final projectName = "#{_JIRA_PROJECT_NAME_}"
    @Field final projectKey  = "#{_GITHUB_REPOSITORY_NAME_}"
    @Field final jiraKeyParent = "#{_JIRA_KEY_}"


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

        //def gitCommit = getCommit(env.MasterBuild_JOBNAME, env.MasterBuild_NUMBER, githubProjectName)
        def environment = "dev"
        def TAG = version
        def jiraKeyCustom = extractJiraKey(inputJiraKey)

        stage("Cleanup") {
            echo "gitRepository = ${gitRepository} projectName = ${projectName} projectKey = ${projectKey} jiraKeyParent = ${jiraKeyParent}"
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

            def DIST_ZIP = "${JOB_NAME}.zip"
            def ZIP_NAME = "liferay-dxp.zip"
            sh "cp -f ./build/$DIST_ZIP ./azure/docker/dxp/liferay-dxp/$ZIP_NAME"
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


        stage('Build and Push') {
            dir('azure') {
                sh "cd scripts && ./build-docker-and-push-images.sh"
            }
        }

        stage('Deploy Portal') {
            dir('azure') {
                sh "export KUBECONFIG=\$(find \$PWD -type f | grep 'kubeconfig.yaml') && cd scripts && ./setup-or-upgrade-portal.sh"
            }
        }

        stage('Remove docker images') {
            sh "docker rmi -f \$(docker images | grep '$jiraKeyCustom' | awk '{print \$3}' | uniq)"
        }

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

    def extractJiraKey(inputJiraKey){
        int maxSize = 8
        String jiraKey = inputJiraKey.replaceAll("[^A-Za-z]","").toLowerCase()
        return jiraKey.substring(0, ((jiraKey.length() < maxSize) ? jiraKey.length(): maxSize))
    }