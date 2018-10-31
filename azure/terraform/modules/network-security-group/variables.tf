variable "resource_group_name" {
  description = "Default resource group name that the network will be created in."
}

variable "name" {
  description = "Default name that the network will be created."
}

variable "location" {
  description = "The location/region where the core network will be created. The full list of Azure regions can be found at https://azure.microsoft.com/regions"
}

variable "predefined_rules" {
  type    = "list"
  default = []
}

variable "custom_rules" {
  description = "Security rules for the network security group using this format name = [priority, direction, access, protocol, source_port_range, destination_port_range, source_address_prefix, destination_address_prefix, description]"
  type        = "list"
  default     = []
}

variable "source_address_prefix" {
  type    = "list"
  default = ["*"]
}

variable "destination_address_prefix" {
  type    = "list"
  default = ["*"]
}

variable "tags" {
  description = "The tags to associate with your network security group."
  type        = "map"
  default     = {}
}
