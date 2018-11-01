locals {
  resource_group_name = "${var.resource_group}"
  location            = "${var.location}"
  vm_size             = "${var.vm_size}"
  aks_os_disk_size    = "0"
  agent_count         = "${var.node_count}"
  ssh_public_key      = "../../../config/ssh/id_rsa.pub"
  af_account_name     = "${var.azure_file_account_name}"
  af_shared_name      = "terraformshare"
  af_proxy_share_name = "proxyshare"
  registry_name       = "${var.registry_name}"


  aks_name                          = "${var.aks_name}"
  aks_dns_prefix                    = "${var.aks_dns_prefix}"
  aks_agent_pool_profile_name       = "default"
  aks_os_type                       = "Linux"
  aks_network_plugin                = "azure"
  db_name                           = "${var.db_name}"
  db_fw_name                        = "${format("%s-fwrules", var.service_prefix)}"
  db_server_name                    = "${var.db_server_name}"
  db_service_name                   = "db"
  db_azure_postgres_sku_tier        = "GeneralPurpose"
  db_postgres_version               = "10.0"
  db_enforce_ssl                    = "Disabled"
  db_sku_compute_units              = "${var.db_sku_compute_units}"
  db_disk_size_mb                   = "${var.db_disk_size_mb}"
  db_azure_postgres_sku_family      = "${var.db_azure_postgres_sku_family}"
  db_backup_retention_days          = 7
  db_geo_redundant_backup           = "Enabled"
  db_database_charset               = "UTF8"
  db_database_collation             = "English_United States.1252"
  db_start_ip_address               = "0.0.0.0"
  db_end_ip_address                 = "255.255.255.255"
  subnet_name                       = "${format("%s-subnet", var.service_prefix)}"
  sg_name                           = "${format("%s-nsg", var.service_prefix)}"
  vn_name                           = "${format("%s-vnet", var.service_prefix)}"

  predefined_rules    = [
    {
      name                   = "SSH"
      priority               = "100"
    },
    {
      name                   = "HTTPS"
      priority               = "101"
      source_address_prefix  = "Internet"
    },
    {
      name                   = "HTTP"
      priority               = "102"
      source_address_prefix  = "Internet"
    },
    {
      name                   = "ElasticSearch"
      priority               = "103"
      source_address_prefix  = "Internet"
    }
  ]

  address_space      = "10.0.0.0/8"
  subnet_prefixes    = "10.240.0.0/12"

}