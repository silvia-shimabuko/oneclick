ARG DXP_IMAGE=liferay/liferay-dxp:7.1.10
FROM $DXP_IMAGE

ADD ./patches /docker-init.d/patches
ADD ./patching-tool /docker-init.d/patching-tool
ADD entrypoint-process-patches-directory.sh /usr/local/bin/

RUN ENTRYPOINT_DIR="/docker-init.d" entrypoint-process-patches-directory.sh
