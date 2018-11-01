# Configurations Files

## userinput.tfvars

This is the main configuration file, used to setup several infrastructure and even some portal configurations.

Here's the reference for the possible input values:

|       Parameter               |                             Description                                                                            |
| ----------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| resource_group                | The resource group for all of the created infrastructure
| location                      | The Azure location/region for the infrastructure (ex. eastus, westus, etc)
| service_prefix                | This prefix is used in front of several resource names, like vnet, etc
| azure_subscription_id         | This the Azure subscription id where the whole setup will take
| azure_tenant_id               | The principal tenant id
| azure_client_id               | The principal appId |
| azure_client_secret           | The principal passwword |
| aks_admin_username            | Kubernetes administrator username. This name must comply to AKS restrictions |
| aks_name                      | AKS name |
| aks_dns_prefix                | AKS dns prefix |
| db_admin_username             | Postgres SQL admin user |
| db_password                   | Postgres SQL admin password. This password must comply to Azure password formation policies
| db_name                       | Postgres database name
| db_server_name                | Postgres database host server name. This name must be unique
| db_fw_name                    | Database Firewall name
| db_azure_postgres_sku_family  | Database instance generation. Can be either Gen4 or Gen5. Details [here](https://docs.microsoft.com/en-us/azure/postgresql/concepts-pricing-tiers)
| db_sku_compute_units          | Database instance core count. Not all combinations are valid, check the above link.
| db_disk_size_mb               | The size of the database disk in megabytes.
| registry_name                 | The name of the docker registry that will host DXP images
| vm_size                       | The nodes VM instance type. See [azure reference](https://docs.microsoft.com/en-us/azure/virtual-machines/windows/sizes-memory)
| node_count                    | Number of nodes to run the cluster
| dxp_image_name                | The name that will be used by DXP docker images
| proxy_image_name              | The name that will be used by proxy docker images
| azure_file_account_name       | The name of the storage account that will be used to host the portal files.
| dxp_replica_count             | The number of portal pods
| backend_storage_account_name  | Backend storage account name. This is an account where terraform state will be stored
| ssh_public_key                | The directory where the ssh public key will be. Defaults to config/ssh.

Note: if any of these values are changed, you should run setup_infrastructure.sh to update the infrastructure.
Be careful of running database details and running setup_infrastructure.sh, and changes can cause the database to be lost.
After it is ready, better not change at all.

## Azure AD principal : where to get the values

If you ran az ad sp create-for-rbac, you would get the following output:

```json
{
  "appId": "...",
  "displayName": "A_Principal_Name",
  "name": "http://A_Principal_Name",
  "password": "the-password",
  "tenant": "the-tenant-id"
}
```

- appId will be set to azure_client_id
- tenant will be set to azure_tenant_id
- password will be set to azure_client_secret