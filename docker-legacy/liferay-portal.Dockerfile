FROM oracle-jdk-8:latest

# Install Liferay
ADD liferay-docker.tar.gz /opt/liferay
# Install MS SQL JDBC Driver
ADD build/mysql.jar /opt/liferay/tomcat-8.0.32/lib/ext
ADD run-liferay.sh /opt/liferay/run-liferay.sh

RUN chmod +x /opt/liferay/run-liferay.sh

# Install and Setup Apache
RUN apt-get update && apt-get install -y --no-install-recommends \
		apache2 \
		ssl-cert \
		vim \
                ImageMagick \
                ghostscript \
	&& rm -rf /var/lib/apt/lists/*
RUN a2disconf serve-cgi-bin
RUN a2enmod proxy_ajp
RUN a2enmod rewrite
RUN a2enmod ssl
ADD apache-proxy.conf /etc/apache2/sites-enabled/000-default.conf
ADD apache-ssl-proxy.conf /etc/apache2/sites-enabled/default-ssl.conf

# Ports
EXPOSE 80
EXPOSE 443

# EXEC
ENTRYPOINT ["/opt/liferay/run-liferay.sh"]
#CMD ["run"]
