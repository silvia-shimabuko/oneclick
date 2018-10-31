variable "location" {}

variable "service_name" {
  description = "Service this resource belongs to"
}

variable "resource_group_name" {
  description = "Name of the resource group to place the database into. Optional and will create it's own if omitted"
}

variable "db_server_name" {
  description = "Name of the postgres server"
}

variable "db_database_name" {
  description = "Name of the postgres database"
}

variable "db_admin_username" {
  description = "Username for the database admin user"
}

variable "db_admin_password" {
  description = "Password for the database admin user"
}

variable "azure_postgres_sku_tier" {
  description = "Azure SKU tier reference for the DB (Preview - Basic and Standard available)"
}

variable "sku_compute_units" {
  description = "Azure compute units. 100 ~= 1 core. Default 100"
}

variable "db_disk_size_mb" {
  description = "Size of the DB storage in MB - allowed values (basic) 51200, 179200, 307200. 435200 / (Standard) 128000, 256000, 384000 etc..."
}

variable "postgres_version" {
  description = "Version of postgres to use.  Currently 9.5 and 9.6 supported. Defaults to 9.6"
}

variable "enforce_ssl" {
  description = "Should the server enforce SSL on connections to the database. Defaults to Enabled"
}

variable "azure_postgres_sku_family" {
  description = "Azure SKU generaition reference for the DB - one of Gen4 or Gen5"
}

variable "backup_retention_days" {
  description = "Number of days to retain backup restore points for.  Default is 7"
}

variable "geo_redundant_backup" {
  description = "Use GRS based backup? (NB - the platform will decide where the backups are held, it is not configurable)"
}

variable "database_charset" {}

variable "database_collation" {}

variable "db_firewall_name" {
  description = "Name of the postgres firewall rule"
}

variable "start_ip_address" {}

variable "end_ip_address" {}
