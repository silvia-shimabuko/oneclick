resource "local_file" "kubeconfig" {
  content  = "${module.kubernetes_cluster.kube_config_raw}"
  filename = "../../../config/out/kubeconfig.yaml"
}

resource "local_file" "azurefile" {
  content = "${jsonencode(map(
        "storage_sharename", "${module.azure-file.share_name}",
        "access_key",  "${module.azure-file.access_key}"
    ))}"

  filename = "../../../config/out/azurefile.json"
}

resource "local_file" "proxyfile" {
  content = "${jsonencode(map(
        "proxy_storage_sharename", "${module.azure-file.proxy_share_name}"
    ))}"

  filename = "../../../config/out/proxyfile.json"
}
