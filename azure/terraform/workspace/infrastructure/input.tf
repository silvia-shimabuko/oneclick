variable "azure_subscription_id" {
  description = "id field obtained through azure cli: az login"
}

variable "azure_client_id" {
  description = "appId field created through azure cli: az ad sp create-for-rbac --role=\"Contributor\" --scopes=\"/subscriptions/00000000-0000-0000-0000-000000000000\""
}

variable "azure_client_secret" {
  description = "password field created through azure cli: az ad sp create-for-rbac --role=\"Contributor\" --scopes=\"/subscriptions/00000000-0000-0000-0000-000000000000\""
}

variable "azure_tenant_id" {
  description = "tenantId field obtained through azure cli: az login"
}

variable "aks_admin_username" {}

variable "db_admin_username" {}

variable "db_password" {}

variable "db_name" {}

variable "azure_file_account_name" {}

variable "registry_name" {}

variable "db_server_name" {}

variable "resource_group" {}

variable "location" {}

variable "vm_size" {}

variable "node_count" {}

variable "service_prefix" {}

variable "aks_name" {}

variable "aks_dns_prefix" {}

variable "db_sku_compute_units" {}

variable "db_disk_size_mb" {}

variable "db_azure_postgres_sku_family" {}
