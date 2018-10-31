terraform {
  required_version = ">= 0.11.7"
}

provider "azurerm" {
  version         = "1.13.0"
  subscription_id = "${var.azure_subscription_id}"
  client_id       = "${var.azure_client_id}"
  client_secret   = "${var.azure_client_secret}"
  tenant_id       = "${var.azure_tenant_id}"
}

provider "helm" {
  kubernetes {
    config_path = "${local_file.kubeconfig.filename}"
  }
}
