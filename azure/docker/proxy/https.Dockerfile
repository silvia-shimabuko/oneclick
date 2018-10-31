FROM buildpack-deps:jessie-scm

RUN apt-get update \
    && apt-get install -y --no-install-recommends apache2 ssl-cert \
    && rm -rf /var/lib/apt/lists/* \
    && a2disconf serve-cgi-bin \
    && a2enmod proxy_ajp \
    && a2enmod rewrite \
    && a2enmod proxy \
    && a2enmod proxy_http \
    && a2enmod ssl \
    && a2enmod headers
    
ADD 000-default.conf /etc/apache2/sites-enabled/000-default.conf
ADD 000-default-ssl.conf /etc/apache2/sites-enabled/000-default-ssl.conf
ADD options-ssl-apache.conf /etc/apache2/conf-available/options-ssl-apache.conf
ADD options-http-ssl.conf /etc/apache2/conf-available/http-options.conf

EXPOSE 80
EXPOSE 443

ENV PROXY_TARGET=localhost
ENV PROXY_TARGET_HTTP_PORT=8080

COPY apache-proxy-start.sh /opt/
RUN chmod +x /opt/apache-proxy-start.sh
ENTRYPOINT ["/opt/apache-proxy-start.sh"]
