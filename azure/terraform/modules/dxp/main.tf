resource "helm_release" "liferay-dxp" {
  name         = "liferay-dxp"
  chart        = "${replace(path.module,"/.*.terraform/",".terraform")}/charts/"
  timeout      = 1800 # seconds -> 30 minutes
  force_update = true
  keyring      = "../../../config/pubring.gpg"

  set {
    name  = "liferay.image"
    value = "${var.dxp_image}"
  }

  set {
    name  = "proxy.image"
    value = "${var.proxy_image}"
  }

  set {
    name = "liferay.replicas"
    value = "${var.replicas}"
  }

  set {
    name  = "liferay.storage.sharename"
    value = "${var.storage_sharename}"
  }

  set {
    name  = "proxy.storage.sharename"
    value = "${var.proxy_storage_sharename}"
  }
}
