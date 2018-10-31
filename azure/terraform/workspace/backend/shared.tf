locals {
  location                         = "${var.location}"
  resource_group                   = "${var.resource_group}"
  backend_storage_account_name     = "${var.backend_storage_account_name}"
  backend_container_name           = "desjardins-stater"
  backend_account_tier             = "Standard"
  backend_account_replication_type = "RAGRS"
  backend_container_access_type    = "blob"
}
