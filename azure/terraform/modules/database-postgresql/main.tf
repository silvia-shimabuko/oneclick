resource "azurerm_postgresql_server" "server" {
  name                = "${var.db_server_name}"
  location            = "${var.location}"
  resource_group_name = "${var.resource_group_name}"

  sku {
    name     = "${var.azure_postgres_sku_tier == "GeneralPurpose" ? "GP" : var.azure_postgres_sku_tier == "Basic" ? "B" : "MO" }_${var.azure_postgres_sku_family}_${var.sku_compute_units}"
    tier     = "${var.azure_postgres_sku_tier}"
    capacity = "${var.sku_compute_units}"
    family   = "${var.azure_postgres_sku_family}"
  }

  administrator_login          = "${var.db_admin_username}"
  administrator_login_password = "${var.db_admin_password}"
  version                      = "${var.postgres_version}"
  ssl_enforcement              = "${var.enforce_ssl}"

  storage_profile {
    storage_mb            = "${var.db_disk_size_mb}"
    backup_retention_days = "${var.backup_retention_days}"
    geo_redundant_backup  = "${var.geo_redundant_backup}"
  }
}

resource "azurerm_postgresql_database" "database" {
  name                = "${var.db_database_name}"
  resource_group_name = "${var.resource_group_name}"
  server_name         = "${azurerm_postgresql_server.server.name}"
  charset             = "${var.database_charset}"
  collation           = "${var.database_collation}"
}

resource "azurerm_postgresql_firewall_rule" "fw" {
  name                = "${var.db_firewall_name}"
  resource_group_name = "${var.resource_group_name}"
  server_name         = "${azurerm_postgresql_server.server.name}"
  start_ip_address    = "${var.start_ip_address}"
  end_ip_address      = "${var.end_ip_address}"
}
