# Elasticsearch Helm Chart

This chart uses a standard Docker image of Elasticsearch (docker.elastic.co/elasticsearch/elasticsearch-oss) and uses a service pointing to the master's transport port for service discovery.
Elasticsearch does not communicate with the Kubernetes API, hence no need for RBAC permissions.

## How to test the configuration on Azure

### Create a cluster on Azure

```
$ az group create --name myAKSCluster --location eastus
$ az aks create --resource-group myAKSCluster --name myAKSCluster --node-count 1 --node-vm-size=Standard_E2s_v3 --generate-ssh-keys
$ az aks install-cli
$ az aks get-credentials --resource-group myAKSCluster --name myAKSCluster
```

### Configure helm

To configure helm before you can deploy Helm in an RBAC-enabled cluster, you need a service account and role binding for the Tiller service.

Create a file named helm-rbac.yaml and copy in the following YAML:

```
apiVersion: v1
kind: ServiceAccount
metadata:
  name: tiller
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: tiller
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: tiller
    namespace: kube-system
```

Then create the service account and role binding with the `kubectl create` command and configure helm

```
$ kubectl create -f helm-rbac.yaml
$ helm init --service-account tiller
```

### Run Elasticsearch chart

```
$ helm install ./elasticsearch
```

## How to test the configuration on local machine

### Create a cluster with minikube

Install and configure minikube then start minikube with 4Gb of RAM

```
$ minikube start --memory 4000
```

### Configure helm

```
$ helm init
```

### Run Elasticsearch chart

```
$ helm install ./elasticsearch
```

## Configuration

The following table lists the configurable parameters of the elasticsearch chart and their default values.

|              Parameter               |                             Description                             |               Default                |
| ------------------------------------ | ------------------------------------------------------------------- | ------------------------------------ |
| `appVersion`                         | Application Version (Elasticsearch)                                 | `6.3.1`                              |
| `image.repository`                   | Container image name                                                | `docker.elastic.co/elasticsearch/elasticsearch-oss` |
| `image.tag`                          | Container image tag                                                 | `6.3.1`                              |
| `image.pullPolicy`                   | Container pull policy                                               | `Always`                             |
| `cluster.name`                       | Cluster name                                                        | `elasticsearch`                      |
| `cluster.kubernetesDomain`           | Kubernetes cluster domain name                                      | `cluster.local`                      |
| `cluster.xpackEnable`                | Writes the X-Pack configuration options to the configuration file   | `false`                              |
| `cluster.config`                     | Additional cluster config appended                                  | `{}`                                 |
| `cluster.env`                        | Cluster environment variables                                       | `{}`                                 |
| `client.name`                        | Client component name                                               | `client`                             |
| `client.replicas`                    | Client node replicas (deployment)                                   | `2`                                  |
| `client.resources`                   | Client node resources requests & limits                             | `{} - cpu limit must be an integer`  |
| `client.priorityClassName`           | Client priorityClass                                                | `nil`                                |
| `client.heapSize`                    | Client node heap size                                               | `512m`                               |
| `client.podAnnotations`              | Client Deployment annotations                                       | `{}`                                 |
| `client.nodeSelector`                | Node labels for client pod assignment                               | `{}`                                 |
| `client.tolerations`                 | Client tolerations                                                  | `{}`                                 |
| `client.serviceAnnotations`          | Client Service annotations                                          | `{}`                                 |
| `client.serviceType`                 | Client service type                                                 | `ClusterIP`                          |
| `master.exposeHttp`                  | Expose http port 9200 on master Pods for monitoring, etc            | `false`                              |
| `master.name`                        | Master component name                                               | `master`                             |
| `master.replicas`                    | Master node replicas (deployment)                                   | `2`                                  |
| `master.resources`                   | Master node resources requests & limits                             | `{} - cpu limit must be an integer`  |
| `master.priorityClassName`           | Master priorityClass                                                | `nil`                                |
| `master.podAnnotations`              | Master Deployment annotations                                       | `{}`                                 |
| `master.nodeSelector`                | Node labels for master pod assignment                               | `{}`                                 |
| `master.tolerations`                 | Master tolerations                                                  | `{}`                                 |
| `master.heapSize`                    | Master node heap size                                               | `512m`                               |
| `master.name`                        | Master component name                                               | `master`                             |
| `master.persistence.enabled`         | Master persistent enabled/disabled                                  | `true`                               |
| `master.persistence.name`            | Master statefulset PVC template name                                | `data`                               |
| `master.persistence.size`            | Master persistent volume size                                       | `4Gi`                                |
| `master.persistence.storageClass`    | Master persistent volume Class                                      | `nil`                                |
| `master.persistence.accessMode`      | Master persistent Access Mode                                       | `ReadWriteOnce`                      |
| `data.exposeHttp`                    | Expose http port 9200 on data Pods for monitoring, etc              | `false`                              |
| `data.replicas`                      | Data node replicas (statefulset)                                    | `3`                                  |
| `data.resources`                     | Data node resources requests & limits                               | `{} - cpu limit must be an integer`  |
| `data.priorityClassName`             | Data priorityClass                                                  | `nil`                                |
| `data.heapSize`                      | Data node heap size                                                 | `1536m`                              |
| `data.persistence.enabled`           | Data persistent enabled/disabled                                    | `true`                               |
| `data.persistence.name`              | Data statefulset PVC template name                                  | `data`                               |
| `data.persistence.size`              | Data persistent volume size                                         | `30Gi`                               |
| `data.persistence.storageClass`      | Data persistent volume Class                                        | `nil`                                |
| `data.persistence.accessMode`        | Data persistent Access Mode                                         | `ReadWriteOnce`                      |
| `data.podAnnotations`                | Data StatefulSet annotations                                        | `{}`                                 |
| `data.nodeSelector`                  | Node labels for data pod assignment                                 | `{}`                                 |
| `data.tolerations`                   | Data tolerations                                                    | `{}`                                 |
| `data.terminationGracePeriodSeconds` | Data termination grace period (seconds)                             | `3600`                               |
| `data.antiAffinity`                  | Data anti-affinity policy                                           | `soft`                               |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.

In terms of Memory resources you should make sure that you follow that equation:

- `${role}HeapSize < ${role}MemoryRequests < ${role}MemoryLimits`

The YAML value of cluster.config is appended to elasticsearch.yml file for additional customization ("script.inline: on" for example to allow inline scripting)