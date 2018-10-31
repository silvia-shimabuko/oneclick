variable "account_name" {}

variable "share_name" {}

variable "proxy_share_name" {}

variable "resource_group_name" {}

variable "location" {}

variable "account_tier" {
  default = "Standard"
}

variable "account_replication_type" {
  default = "GRS"
}
