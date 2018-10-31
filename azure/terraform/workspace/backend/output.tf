resource "local_file" "backend" {
  content = "${jsonencode(map(
        "storage_account_name", "${azurerm_storage_account.backsa.name}",
        "container_name", "${azurerm_storage_container.backsc.name}",
        "access_key", "${azurerm_storage_account.backsa.primary_access_key}"
  ))}"

  filename = "../../../config/out/backend.json"
}
