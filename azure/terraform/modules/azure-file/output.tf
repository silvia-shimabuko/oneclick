output "share_name" {
  value = "${azurerm_storage_share.share.name}"
}

output "proxy_share_name" {
  value = "${azurerm_storage_share.proxy-share.name}"
}

output "access_key" {
  value = "${azurerm_storage_account.account.primary_access_key}"
}
