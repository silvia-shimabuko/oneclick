variable "resource_group_name" {
  description = "Default resource group name that the network will be created in."
}

variable "name" {
  description = "Default name that the network will be created."
}

variable "location" {
  description = "The location/region where the core network will be created. The full list of Azure regions can be found at https://azure.microsoft.com/regions"
}

variable "subnet_name" {
}

variable "security_group_id" {
  description = "The security group name."
}

variable "address_space" {
  type        = "list"
  description = "The address space that is used by the virtual network."
  default     = ["10.0.0.0/16"]
}

variable "subnet_prefixes" {
  description = "The address prefix to use for the subnet."
  default     = "10.0.1.0/24"
}