# Create a container registry
resource "azurerm_container_registry" "docker" {
  name                = "${var.name}"
  location            = "${var.location}"
  resource_group_name = "${var.resource_group_name}"
  sku                 = "${var.sku}"
}
