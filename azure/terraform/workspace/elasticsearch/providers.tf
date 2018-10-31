terraform {
  required_version = ">= 0.11.7"
}

provider "helm" {
  kubernetes {
    config_path = "${local.kube_config_file}"
  }
}
