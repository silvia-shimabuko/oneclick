#!/bin/bash
set -e

function getfield {
    grep "$1" | sed 's/^[^=]*=//'
}

ROOT_XML_DIR=$LIFERAY_HOME/tomcat/conf/Catalina/localhost/
USER_CONFIG_DIR=$LIFERAY_HOME/userconfig
USER_PROPERTIES_FILE=$USER_CONFIG_DIR/portal-ext.user.properties

if [[ ! -e $USER_PROPERTIES_FILE ]]; then
    echo "portal-ext.user.properties not found."
    exit -1
fi

export DB_URL=$(cat $USER_PROPERTIES_FILE  | getfield jdbc.default.url )
export DB_USER=$(cat $USER_PROPERTIES_FILE | getfield jdbc.default.username )
export DB_PASSWORD=$(cat $USER_PROPERTIES_FILE | getfield jdbc.default.password )

[[ -z "${DB_URL}" ]] && { echo "Portal 'URL' has not been configured in config/portal-ext.user.properties"; exit -1; }
[[ -z "${DB_USER}" ]] && { echo "Portal 'username' has not been configured in config/portal-ext.user.properties"; exit -1; }
[[ -z "${DB_PASSWORD}" ]] && { echo "Portal 'password' has not been configured in config/portal-ext.user.properties"; exit -1; }

cat $ROOT_XML_DIR/ROOT.xml.template | envsubst.sh > $ROOT_XML_DIR/ROOT.xml

entrypoint-run-portal.sh
