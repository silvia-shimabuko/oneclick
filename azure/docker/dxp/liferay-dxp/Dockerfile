# Build Liferay Portal CE
FROM debian:8.11 as baseimage

ENV \
  LC_ALL="en_US.UTF-8" \
  DEBIAN_FRONTEND="noninteractive" \
  LIFERAY_HOME="/opt/liferay" \
  JAVA_HOME="/opt/jdk"

ENV \
  CATALINA_HOME="$LIFERAY_HOME/tomcat" \
  JRE_HOME="$JAVA_HOME/jre"

ENV \
  PATH="$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH"

RUN \
  echo "deb http://http.debian.net/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list && \
  apt-get update -y && \
  apt-get -qq install -y \
    curl locales software-properties-common telnet tree unzip && \
  echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
  locale-gen en_US.UTF-8 && \
  dpkg-reconfigure locales && \
  apt-get install -t jessie-backports -y --no-install-recommends \
    openjdk-8-jdk="8u171-b11-1~bpo8+1" \
    ca-certificates-java="20161107~bpo8+1" && \
  ln -s /usr/lib/jvm/java-8-openjdk-amd64 /opt/jdk && \
  rm -rf /var/lib/apt/lists/* && \
  apt-get clean && \
  groupadd -r liferay && \
  useradd -r -d /opt/liferay -g liferay liferay

# Build Liferay DXP
FROM baseimage

ARG DXP_FILENAME=liferay-dxp.zip
ADD --chown=liferay:liferay ./$DXP_FILENAME /tmp/$DXP_FILENAME

RUN \
  mkdir -p /docker-init.d && \
  mkdir -p $LIFERAY_HOME && \
  unzip /tmp/$DXP_FILENAME -d /tmp/liferay && \
  if [ -d /tmp/liferay/osgi ]; then  mv /tmp/liferay/* $LIFERAY_HOME/ ; else mv /tmp/liferay/*/* $LIFERAY_HOME/ ; fi && \
  mv $LIFERAY_HOME/tomcat-* $CATALINA_HOME && \
  cp -r $LIFERAY_HOME/data /docker-init.d/data && \
  chmod +x $CATALINA_HOME/bin/*.sh && \
  if [ -d $LIFERAY_HOME/patching-tool ]; then \
      echo "patching.mode=binary\n\
war.path=$LIFERAY_HOME/tomcat/webapps/ROOT/\n\
global.lib.path=$LIFERAY_HOME/tomcat/lib/ext/\n\
liferay.home=$LIFERAY_HOME/\n\
" > $LIFERAY_HOME/patching-tool/default.properties \
  ; fi  && \
  chown -R liferay:liferay $LIFERAY_HOME && \
  rm -rf $LIFERAY_HOME/tools && \
  rm -f $CATALINA_HOME/webapps/ROOT/WEB-INF/patching-backup.zip && \
  rm -rf /tmp/* && \
  rm $LIFERAY_HOME/tomcat/bin/*.bat

ADD ./dxp/isempty.sh /usr/local/bin/isempty
ADD ./dxp/entrypoint-* /usr/local/bin/
ADD ./dxp/entrypoint.sh /usr/local/bin/entrypoint

USER liferay:liferay

HEALTHCHECK --start-period=4m \
  CMD curl -fsS 'http://localhost:8080/c/portal/layout' || exit 1

WORKDIR $LIFERAY_HOME

VOLUME $LIFERAY_HOME/data

EXPOSE 8080/tcp 11311/tcp

ENTRYPOINT [ "entrypoint" ]
