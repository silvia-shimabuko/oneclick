# Liferay Azure Infrasctructure and Deployment Automation

# Tools required to run this project

- [Terraform](https://www.terraform.io/downloads.html) >=0.11.7
- [Kubernetes](https://kubernetes.io/) >=1.4
- [Helm](https://helm.sh/) >=2.9.1
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli?view=azure-cli-latest) >=2.0.44

## How to get started

Start by creating a principal or getting an existing principal details. You will need the following information:

- subscription Id
- appId
- password
- tenant

## Infrastructure

- copy *config/userinput.tfvars.sample* to *config/userinput.tfvars* and make sure to edit all parameters (except ssh_public_key)
- copy the public ssh key file to access the pods to *config/ssh*

Then, go and run:

```sh
scripts/setup-infrastructure.sh
```

This will create all of the required infrastructure in Azure. The whole setup will take between 30 to 60 minutes to complete.

## Building the Docker images

First, read *docker/README.md* for instructions on how to prepare your container workspace. After all is set, you can run:

```sh
scripts/build-docker-and-push-images.sh
```

Finally, you can start up the portal in Kubernetes by running:

```sh
scripts/setup-or-upgrade-portal.sh
```

The portal will take between 5-20 minutes to start. You can verify that it's working by running:

```sh
source scripts/kube-context.sh
$ kubectl get services

NAME                         TYPE           CLUSTER-IP     EXTERNAL-IP     PORT(S)        AGE
es-elasticsearch-client      ClusterIP      10.0.100.203   <none>          9200/TCP       39m
es-elasticsearch-discovery   ClusterIP      10.0.97.210    <none>          9300/TCP       39m
kubernetes                   ClusterIP      10.0.0.1       <none>          443/TCP        45m
liferay                      LoadBalancer   10.0.90.201    40.117.208.20   80:31556/TCP   23m # This is the portal service
```

Browse the portal IP address at 40.117.208.20 to acecss the portal. You must do this to finish portal configuration.

After the first execution, when you check the pods you will see something like this:

```sh
$ kubectl get pods

NAME                                      READY     STATUS    RESTARTS   AGE
es-elasticsearch-client-bd8b8dbf5-f5gqh   1/1       Running   2          49m
es-elasticsearch-data-0                   1/1       Running   0          49m
es-elasticsearch-master-0                 1/1       Running   0          49m
es-elasticsearch-master-1                 1/1       Running   0          45m
liferay-54cb4988f5-wb7b8                  1/1       Running   0          33m
```

You will notice that only one portal pod is running. This is expected in the first execution, as it needs to prepare the database and at this point only a single pod should be running.

## Updates to the portal

Everytime you make changes, like:

- changing the docker images
- changes to *config/portal-ext.user.properties*

You should run *update-portal.sh* to apply the changes.

### update-portal vs setup-or-upgrade-portal

There are two scripts to start the portal, *start-portal* and *setup-or-upgrade-portal*.

You should run setup-or-upgrade-portal when:

- This is the first time you start up the portal
- When you applied a patch

When running setup-or-upgrade-portal, only a single instance will be started, to avoid issues when creating or updating the database schemes.

In other situations, you will prefer to run update-portal insted, which will respect your replica count.

## Portal configurations

You can override any portal configurations by editing the *config/portal-ext.user.properties* file. You can add any valid portal property.

Here's a sample on how you'd change the database configuration:

```sh
jdbc.default.url=MY_CUSTOM_DATABASE
jdbc.default.username=SOMEUSER
jdbc.default.password=SOMEPASSWORD
```

## Portal https configuration

This portal can be configured to run with https. To enable https, you should provide a SSL Certificate File (cert.pem) and SSL Certificate Key File (key.pem). Read [README.md](config/certs/README.md) for details. If no certificates are found, a standard http proxy be used.

If you already setup the portal and add the certificates later, you have to rebuild the images, so the proxy will be properly configured with https.

## Project structure

This project consists of all required files to do the following:

- build your custom DXP portal docker image
- setup Azure infrastructure to run the given docker images
- deploy the DXP and Elastic Search

### docker

This directory contains all required Docker files to create the required images. Read [README.md](docker/README.md) for details.
Before setting up the infrastructure the first time, you need to have the images build and pushed to azure registry.

### terraform

Set of files to create the infrastructure and deploy the portal. Read [README.md](terraform/README.md) for details.

### scripts

Set of scripts to easy infrascrtucure and deploy management.

## Managing kubernetes

Before acessing kubernetes, run:

```sh
source scripts/kube-context.sh
```

This will point your current kubernetes context to the portal kubernetes cluster.

## Sharing the workspace

If you want to give someone else access to the same environment, copy the following files:

```
config/*
```

## Troubleshooting

- "authentication required" when pushing images

Make sure you performed an Azure login

## Tips

- Run source `config/kube-context.sh` to manage your kubernetes cluster
- Setup kubernetes autocomplete by running `source <(kubectl completion bash)` [ref](https://kubernetes.io/docs/tasks/tools/install-kubectl/#enabling-shell-autocompletion)
- Open the kubernetes dashboard by running `kubectl proxy` and the accessing [this url](http://localhost:8001/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy/)