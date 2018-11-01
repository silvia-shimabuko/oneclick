variable "client_id" {
}

variable "client_secret" {
}

variable "subnet_id" {
}

variable resource_group_name {
}

variable "vm_size" {
}

variable "os_disk_size" {
}

variable "agent_count" {
}

variable "aks_admin_username" {
}

variable "ssh_public_key" {
}

variable "location" {
}

variable "agent_pool_profile_name" {
    default = "default"
}

variable "name" {
}

variable "dns_prefix" {
}

variable "os_type" {
    default = "Linux"
}

variable "network_plugin" {
    default = "azure"
}

variable http_application_routing {
    default = false
}