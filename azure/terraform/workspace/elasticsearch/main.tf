module "elasticsearch" {
  source           = "../../modules/elasticsearch"
  image_repository = "${local.image_repository}"
}
