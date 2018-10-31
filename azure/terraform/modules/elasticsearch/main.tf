resource "helm_release" "es" {
  name         = "es"
  chart        = "${path.module}/charts/"
  timeout      = 1200
  force_update = true

  set {
    name  = "image.repository"
    value = "${var.image_repository}"
  }

  set {
    name  = "image.tag"
    value = "${var.image_tag}"
  }

  set {
    name  = "cluster.env.MINIMUM_MASTER_NODES"
    value = "${var.master_replicas}"
  }

  set {
    name  = "master.replicas"
    value = "${var.master_replicas}"
  }

  set {
    name  = "client.replicas"
    value = "${var.client_replicas}"
  }

  set {
    name  = "data.replicas"
    value = "${var.data_replicas}"
  }

  set {
    name  = "cluster.name"
    value = "LiferayElasticsearchCluster"
  }
}
