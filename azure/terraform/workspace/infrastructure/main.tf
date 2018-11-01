module "security_group" {
  source              = "../../modules/network-security-group"
  resource_group_name = "${local.resource_group_name}"
  location            = "${local.location}"
  name                = "${local.sg_name}"
  predefined_rules    = "${local.predefined_rules}"
}

module "virtual_network" {
  source              = "../../modules/vnet"
  resource_group_name = "${local.resource_group_name}"
  location            = "${local.location}"
  name                = "${local.vn_name}"
  address_space       = "${list(local.address_space)}"
  subnet_prefixes     = "${local.subnet_prefixes}"
  security_group_id   = "${module.security_group.network_security_group_id}"
  subnet_name         = "${local.subnet_name}"
}

module "kubernetes_cluster" {
  source                  = "../../modules/kubernetes-cluster"
  resource_group_name     = "${local.resource_group_name}"
  location                = "${local.location}"
  subnet_id               = "${module.virtual_network.vnet_subnet_id}"
  vm_size                 = "${local.vm_size}"
  os_disk_size            = "${local.aks_os_disk_size}"
  agent_count             = "${local.agent_count}"
  aks_admin_username      = "${var.aks_admin_username}"
  ssh_public_key          = "${local.ssh_public_key}"
  client_id               = "${var.azure_client_id}"
  client_secret           = "${var.azure_client_secret}"
  name                    = "${local.aks_name}"
  dns_prefix              = "${local.aks_dns_prefix}"
}

module "registry" {
  source              = "../../modules/registry"
  name                = "${local.registry_name}"
  location            = "${local.location}"
  resource_group_name = "${local.resource_group_name}"
}

module "azure-file" {
  source              = "../../modules/azure-file"
  resource_group_name = "${local.resource_group_name}"
  location            = "${local.location}"
  account_name        = "${local.af_account_name}"
  share_name          = "${local.af_shared_name}"
  proxy_share_name    = "${local.af_proxy_share_name}"
}

module "dxp-userconfig" {
  source              = "../../modules/dxp-userconfig"
}

module "database_postgresql" {
  source                    = "../../modules/database-postgresql"
  resource_group_name       = "${local.resource_group_name}"
  location                  = "${local.location}"
  service_name              = "${local.db_service_name}"
  db_admin_username         = "${var.db_admin_username}"
  db_admin_password         = "${var.db_password}"
  db_server_name            = "${local.db_server_name}"
  db_database_name          = "${local.db_name}"
  db_firewall_name          = "${local.db_fw_name}"
  azure_postgres_sku_tier   = "${local.db_azure_postgres_sku_tier}"
  sku_compute_units         = "${local.db_sku_compute_units}"
  db_disk_size_mb           = "${local.db_disk_size_mb}"
  postgres_version          = "${local.db_postgres_version}"
  enforce_ssl               = "${local.db_enforce_ssl}"
  azure_postgres_sku_family = "${local.db_azure_postgres_sku_family}"
  backup_retention_days     = "${local.db_backup_retention_days}"
  geo_redundant_backup      = "${local.db_geo_redundant_backup}"
  database_charset          = "${local.db_database_charset}"
  database_collation        = "${local.db_database_collation}"
  start_ip_address          = "${local.db_start_ip_address}"
  end_ip_address            = "${local.db_end_ip_address}"
}
