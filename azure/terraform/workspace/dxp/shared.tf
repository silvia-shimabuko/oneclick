data "external" "azure-file" {
  program = ["cat", "../../../config/out/azurefile.json"]

  query = {
    storage_sharename = "storage_sharename"
  }
}

data "external" "proxy-file" {
  program = ["cat", "../../../config/out/proxyfile.json"]

  query = {
    proxy_storage_sharename = "proxy_storage_sharename"
  }
}

locals {
  kube_config_file = "../../../config/out/kubeconfig.yaml"

  dxp_image               = "${format("%s.azurecr.io/%s",var.registry_name,var.dxp_image_name)}"
  proxy_image             = "${format("%s.azurecr.io/%s",var.registry_name,var.proxy_image_name)}"
  storage_sharename       = "${data.external.azure-file.result.storage_sharename}"
  proxy_storage_sharename = "${data.external.proxy-file.result.proxy_storage_sharename}"
}
