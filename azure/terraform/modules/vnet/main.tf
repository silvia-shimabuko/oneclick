resource "azurerm_virtual_network" "vnet" {
  name                = "${var.name}"
  location            = "${var.location}"
  address_space       = "${var.address_space}"
  resource_group_name = "${var.resource_group_name}"
}

resource "azurerm_subnet" "subnet" {
  name                      = "${var.subnet_name}"
  resource_group_name       = "${var.resource_group_name}"
  network_security_group_id = "${var.security_group_id}"
  address_prefix            = "${var.subnet_prefixes}"
  virtual_network_name      = "${azurerm_virtual_network.vnet.name}"
}