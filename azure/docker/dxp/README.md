# DXP Dockerfiles

Layers:

## liferay-dxp

Defines the vanilla portal.

## liferay-dxp-patches

Generates a container applying patches

## liferay-dxp-customizations

This will generate the final container, ready to run on azure.

## How to use this project

Usually, you should run *scripts/build-docker-and-push-images.sh*, but you can also run *build-dxp-customizations-and-push.sh* to build and push only the portal