data "external" "azure-file" {
  program = ["cat", "../../../config/out/azurefile.json"]

  query = {
    storage_sharename = "storage_sharename"
  }
}

locals {
  kube_config_file = "../../../config/out/kubeconfig.yaml"

  storage_sharename = "${data.external.azure-file.result.storage_sharename}"
  image_repository  = "${format("%s.azurecr.io/elasticsearch", var.registry_name)}"  
}
