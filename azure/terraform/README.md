# Terraform Orchestrator project

This project is responsible to orchestrate Azure infrastructure and all services necessary to run DXP Portal.

There are three directories:

- modules

This package contains all terraform's modules necessaries to setup the Azure infrastructure, including definition of the deployables (DXP, Elastic Search).

- workspace

The workspace bounds all modules and variables.

## Infrastructure customization

### Shared parameters

The parameters of the [shared.tf](workspace/infrastructure/shared.tf) file make use of Local Value Configuration. Most of the time, you should leave these files alone and customize your needs through the [userinput.tfvars](configuserinput.tfvars) file.

Just for reference, here are the existing variables:

|              Parameter               |                             Description                                                                            |
| ------------------------------------ | ------------------------------------------------------------------------------------------------------------------ |
| `resource_groups`                    | Resource group name that the database will be created in.                                                          |
| `locations`                          | The location/region where the database and server are created.                                                     |
| `vms_size`                           | The size of each VM in the Agent Pool (e.g. Standard_F1).                                                          |
| `aks_os_disk_sizes`                  | The Agent Operating System disk size in GB.                                                                        |
| `agent_counts`                       | Number of Agents (VMs) in the Pool. Possible values must be in the range of 1 to 50 (inclusive). Defaults to 1.    |
| `aks_name`                           | The name of the Container Service instance to create.                                                        |
| `aks_dns_prefix`                     | The DNS Prefix to use for the Container Service master nodes                                                   |
| `aks_agent_pool_profile_name`        | Unique name of the agent pool profile in the context of the subscription and resource group.                  |
| `ssh_public_keys`                    | The Public SSH Key used to access the cluster.                                                                     |
| `aks_os_type`                        | The Operating System used for the Agents. Possible values are Linux and Windows.                                   |
| `aks_network_plugin`                 | Network plugin to use for networking. Currently supported values are azure and kubenet                             |
| `db_server_name`                     | Specifies the name of the PostgreSQL Server.          |
| `db_azure_postgres_sku_tier`         | The tier of the particular SKU. Possible values are Basic, GeneralPurpose, and MemoryOptimized. For more information see the [product documentation](https://docs.microsoft.com/en-us/azure/postgresql/concepts-pricing-tiers).            |
| `db_sku_compute_units`               | The scale up/out capacity, representing server's compute units.                        |
| `db_azure_postgres_sku_family`       | The family of hardware Gen4 or Gen5, before selecting your family check the product documentation for availability in your region.                     |
| `db_postgres_version`                | Specifies the version of PostgreSQL to use. Valid values are 9.5, 9.6, and 10.0.                   |
| `db_enforce_ssl`                     | Specifies if SSL should be enforced on connections. Possible values are Enabled and Disabled.      |
| `db_disk_size_mb`                    | Max storage allowed for a server. Possible values are between 5120 MB(5GB) and 1048576 MB(1TB) for the Basic SKU and between 5120 MB(5GB) and 4194304 MB(4TB) for General Purpose/Memory Optimized SKUs.           |
| `db_backup_retention_days`           | Backup retention days for the server, supported values are between 7 and 35 days.                  |
| `db_geo_redundant_backup`            | Enable Geo-redundant or not for server backup. Valid values for this property are Enabled or Disabled, not supported for the basic tier.           |
| `db_name`                            | Specifies the name of the PostgreSQL Database, which needs to be a [valid PostgreSQL identifier](https://www.postgresql.org/docs/current/static/sql-syntax-lexical.html#SQL-SYNTAX-IDENTIFIERS).                        |
| `db_database_charset`                | Specifies the Charset for the PostgreSQL Database, which needs to be a [valid PostgreSQL Charset](https://www.postgresql.org/docs/current/static/multibyte.html).
| `db_database_collation`              | Specifies the Collation for the PostgreSQL Database, which needs to be a [valid PostgreSQL Collation](https://www.postgresql.org/docs/current/static/collation.html).       |
| `db_fw_name`                         | Specifies the name of the PostgreSQL Firewall Rule.                      |
| `db_start_ip_address`                | Specifies the Charset for the PostgreSQL Database.                                                  |
| `db_end_ip_address`                  | Specifies the End IP Address associated with this Firewall Rule.                                                    |