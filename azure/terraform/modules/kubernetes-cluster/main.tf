resource "azurerm_kubernetes_cluster" "aks" {
  name                = "${var.name}"
  location            = "${var.location}"
  dns_prefix          = "${var.dns_prefix}"
  resource_group_name = "${var.resource_group_name}"

  linux_profile {
    admin_username = "${var.aks_admin_username}"

    ssh_key {
      key_data = "${file(var.ssh_public_key)}"
    }
  }

  addon_profile {
    http_application_routing {
      enabled = "${var.http_application_routing}"
    }
  }

  agent_pool_profile {
    name            = "${var.agent_pool_profile_name}"
    count           = "${var.agent_count}"
    vm_size         = "${var.vm_size}"
    os_type         = "${var.os_type}"
    os_disk_size_gb = "${var.os_disk_size}"

    # Required for advanced networking
    vnet_subnet_id = "${var.subnet_id}"
  }

  service_principal {
    client_id     = "${var.client_id}"
    client_secret = "${var.client_secret}"
  }

  network_profile {
    network_plugin = "${var.network_plugin}"
  }
}
