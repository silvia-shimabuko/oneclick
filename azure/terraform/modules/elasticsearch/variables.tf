variable "image_repository" {
  default = "docker.io/liferay/lfrgs-liferay-elasticsearch"
}

variable "image_tag" {
  default = "6.1.4"
}

variable "master_replicas" {
  default = 2
}

variable "data_replicas" {
  default = 1
}

variable "client_replicas" {
  default = 1
}
