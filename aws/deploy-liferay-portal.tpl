---
- name: Create Amazon Cluster
  hosts: localhost
  vars:
  - required_envvars:
    - AWS_ACCESS_KEY_ID
    - AWS_SECRET_ACCESS_KEY


  tasks:
  - name: Check environment variables
    loop: "{{ required_envvars }}"
    fail:
      msg: "Missing environment variable: {{ item }}"
    when: item not in ansible_env


  - name: Create IAM Policy
    iam_managed_policy:
      policy_name: #{GITHUB_REPOSITORY_NAME}_EC2Describe_All
      policy: "{{ lookup('file', 'DescribePolicy.json') }}"
      state: present
    register: describePolicy


  - name: Create IAM Role
    iam_role:
      name: #{GITHUB_REPOSITORY_NAME}_ECSTaskExecution
      description: Allows ECS tasks to call AWS services on your behalf.
      assume_role_policy_document: "{{ lookup('file', 'assume-role-policy.json') }}"
      managed_policy:
        - arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
      state: present


  - name: Create Users
    iam_user:
      name: "{{ item }}"
      state: present
    with_items:
      - #{GITHUB_REPOSITORY_NAME}-es
      - #{GITHUB_REPOSITORY_NAME}-s3
      - #{GITHUB_REPOSITORY_NAME}-jgroups
  - name: Create IAM Group for Elastic Search
    iam_group:
      name: #{GITHUB_REPOSITORY_NAME}-es-users
      managed_policy:
        - arn:aws:iam::aws:policy/AmazonEC2FullAccess
        - arn:aws:iam::aws:policy/AmazonECS_FullAccess
      users:
        - #{GITHUB_REPOSITORY_NAME}-es
      state: present
  - name: Create IAM Group for S3
    iam_group:
      name: #{GITHUB_REPOSITORY_NAME}-s3-users
      managed_policy:
        - arn:aws:iam::aws:policy/AmazonS3FullAccess
      users:
        - #{GITHUB_REPOSITORY_NAME}-s3
      state: present
  - name: Create IAM Group for jgroups
    iam_group:
      name: #{GITHUB_REPOSITORY_NAME}-jgroups-users
      managed_policy:
        - "{{ describePolicy.policy.arn }}"
      users:
        - #{GITHUB_REPOSITORY_NAME}-jgroups
      state: present


  - name: Create Cluster
    ecs_cluster:
      name: #{CLUSTER_NAME}
      region: us-east-2
      state: present
    register: ecsCluster


  - name: Create a Cloudformation Stack
    cloudformation:
      stack_name: #{STACK_NAME}
      state: present
      region: us-east-2
      disable_rollback: true
      template: cloudformation.json
      template_parameters:
        EcsAmiId: ami-ce1c36ab
        EcsCluster: #{CLUSTER_NAME}
        EcsInstanceType: t2.xlarge
        KeyName: #{KEY_PAIR_NAME}
        LiferayPortalTaskDefinition: #{TASK_DEFINITION_NAME}
        LiferayPortalServiceName: #{LIFERAY_PORTAL_NAME}
        LiferayPortalDockerImage: #{AWS_ECR_URL}/#{GITHUB_REPOSITORY_NAME}:#{TAG}
        ElasticSearchDockerImage: #{AWS_ECR_URL}/#{GITHUB_REPOSITORY_NAME}-elasticsearch:#{TAG}
        ReverseProxyDockerImage: #{AWS_ECR_URL}/#{GITHUB_REPOSITORY_NAME}-reverseproxy:#{TAG}
        MySQLDockerImage: mysql:5.7
    register: cloudFormation