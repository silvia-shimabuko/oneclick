# Create shared azure files for kubernetes
resource "azurerm_storage_account" "account" {
  name                     = "${var.account_name}"
  resource_group_name      = "${var.resource_group_name}"
  location                 = "${var.location}"
  account_tier             = "${var.account_tier}"
  account_replication_type = "${var.account_replication_type}"
}

resource "azurerm_storage_share" "share" {
  name                 = "${var.share_name}"
  resource_group_name  = "${var.resource_group_name}"
  storage_account_name = "${azurerm_storage_account.account.name}"
}

resource "azurerm_storage_share" "proxy-share" {
  name                 = "${var.proxy_share_name}"
  resource_group_name  = "${var.resource_group_name}"
  storage_account_name = "${azurerm_storage_account.account.name}"
}

resource "helm_release" "storage" {
  name         = "azure-file"
  chart        = "${path.module}/charts/"
  timeout      = 900                      # seconds -> 15 minutes
  force_update = true

  set {
    name  = "liferay.storage.name"
    value = "${base64encode(azurerm_storage_account.account.name)}"
  }

  set {
    name  = "liferay.storage.secret"
    value = "${base64encode(azurerm_storage_account.account.primary_access_key)}"
  }
}
