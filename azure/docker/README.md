# Docker project

This project contains required files to build both the portal and the proxy used by it.

## proxy

This docker build defines the proxy.

## dxp

This is the DXP definition. At the very least, you will need 3 external files to properly build portal version 7.1.10. All of these files can be obtained in the customer portal.

- liferay-dxp-tomcat-7.1.10-ga1-20180703090613030.zip
- liferay-fix-pack-dxp-1-7110.zip
- patching-tool-2.0.8.zip

Drop these files in the following directories:

```
./dxp/liferay-dxp-patches/patching-tool/patching-tool.zip             # Notice that the patching tool must be renamed to this name
./dxp/liferay-dxp-patches/patches/liferay-fix-pack-dxp-1-7110.zip
./dxp/liferay-dxp/liferay-dxp-tomcat-7.1.10-ga1-20180703090613030.zip
```

Also, you must place a valid license here:

```
./dxp/liferay-dxp-customizations/license/
```

Finally, run *scripts/build-docker-and-push-images.sh* to build everything and push the images to Azure.

# Customizations

All customer customizations takes place in the following directory:

```
./dxp/liferay-dxp-customizations
```

Proceed to [README.md](./dxp/liferay-dxp-customizations/README.md) for more details.