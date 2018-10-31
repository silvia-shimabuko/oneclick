resource "helm_release" "liferay-dxp-configmap" {
  name         = "liferay-dxp-configmap"
  chart        = "${replace(path.module,"/.*.terraform/",".terraform")}/charts/"
  timeout      = 60                                                              # seconds -> 1 minute
  force_update = true
  keyring      = "../../../config/pubring.gpg"
}
