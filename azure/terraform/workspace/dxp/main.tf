module "dxp" {
  source                  = "../../modules/dxp"
  dxp_image               = "${local.dxp_image}"
  proxy_image             = "${local.proxy_image}"
  storage_sharename       = "${local.storage_sharename}"
  replicas                = "${var.dxp_replica_count}"
  proxy_storage_sharename = "${local.proxy_storage_sharename}"
}
