variable "rules" {
  description = "Standard set of predefined rules"
  type        = "map"

  # [direction, access, protocol, source_port_range, destination_port_range, description]"
  default = {
    SSH           = ["Inbound", "Allow", "TCP", "*", "22", "SSH"]
    HTTP          = ["Inbound", "Allow", "TCP", "*", "80", "HTTP"]
    HTTPS         = ["Inbound", "Allow", "TCP", "*", "443", "HTTPS"]
    ElasticSearch = ["Inbound", "Allow", "TCP", "*", "9200", "ElasticSearch"]
  }
}
