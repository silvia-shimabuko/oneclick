ARG DXP_IMAGE=liferay/liferay-dxp:7.1.10
FROM $DXP_IMAGE

COPY configs /docker-init.d/config
COPY deploy  /docker-init.d/deploy
COPY license /docker-init.d/license
COPY script  /docker-init.d/script
COPY build-layer.sh /tmp/build-layer.sh

RUN /tmp/build-layer.sh
USER root
RUN rm /tmp/build-layer.sh \
    && rm -rf /docker-init.d/config \
    && rm -rf /docker-init.d/deploy \
    && rm -rf /docker-init.d/license \
    && rm -rf /docker-init.d/script
USER liferay:liferay

ADD envsubst.sh /usr/local/bin/
ADD entrypoint-custom-run-portal.sh /usr/local/bin/

ENTRYPOINT [ "entrypoint-custom-run-portal.sh" ]
