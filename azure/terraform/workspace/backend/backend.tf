resource "azurerm_resource_group" "rg"{
  name     = "${local.resource_group}"
  location = "${local.location}"
}

resource "azurerm_storage_account" "backsa" {
  name                     = "${local.backend_storage_account_name}"
  resource_group_name      = "${azurerm_resource_group.rg.name}"
  location                 = "${local.location}"
  account_tier             = "${local.backend_account_tier}"
  account_replication_type = "${local.backend_account_replication_type}"
}

resource "azurerm_storage_container" "backsc" {
  name                  = "${local.backend_container_name}"
  resource_group_name   = "${azurerm_resource_group.rg.name}"
  storage_account_name  = "${azurerm_storage_account.backsa.name}"
  container_access_type = "${local.backend_container_access_type}"
}
