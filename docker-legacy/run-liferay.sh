#!/bin/sh

# ----------------------------------------------------------------------------------------------------
# INIT
# ----------------------------------------------------------------------------------------------------
export LIFERAY_HOME=/opt/liferay
export TOMCAT=${LIFERAY_HOME}/tomcat-8.0.32
pid=0

# ----------------------------------------------------------------------------------------------------
# TERMINATION EVENTS
# ----------------------------------------------------------------------------------------------------
# SIGTERM-handler
shutdown_handler() {
  echo "shutdown_handler() invoked!"
  if [ $pid -ne 0 ]; then
    echo "shutdown_handler() clean up tasks!"

    echo "Shutdown Tomcat"
    ${TOMCAT}/bin/shutdown.sh

#    local SLEEP_TIME=5
#    local PATTERN=java
#    local JPID=`ps -ef | grep -i $PATTERN| grep -v grep |grep -v sshd | awk -F' ' '{print $2}'`

#    while [ ! -z $JPID ] && [ $JPID -gt 0 ]
#    do
#        echo "The process is still running... waiting" $SLEEP_TIME "seconds."
#        sleep $SLEEP_TIME
#        JPID=`ps -ef | grep -i $PATTERN| grep -v grep |grep -v sshd | awk -F ' ' '{print $2}'`
#    done
#    echo "Attention! The process has been finally stopped OK"

    echo "Shutdown Apache"
    /usr/sbin/apache2ctl stop
  fi

  exit 143; # 128 + 15 -- SIGTERM
}

# ----------------------------------------------------------------------------------------------------
# License (LCS)
# ----------------------------------------------------------------------------------------------------
if [ -e /tmp/*.aatf ]; then
	cp /tmp/*.aatf ${LIFERAY_HOME}/data/
fi

# ----------------------------------------------------------------------------------------------------
# Custom developments
# ----------------------------------------------------------------------------------------------------
files=/tmp/customdev/
if [ -e $files ] && [ "$(ls -A $files)" ]; then
	echo "Copying custom developments to liferay deploy path.";
	cp /tmp/customdev/*.jar ${LIFERAY_HOME}/deploy/
	cp /tmp/customdev/*.war ${LIFERAY_HOME}/deploy/
fi

# FINALIZE
# ----------------------------------------------------------------------------------------------------

# Trap to intercept SIGTERM and to shutdown tomcat

trap 'kill ${!}; shutdown_handler' TERM
trap 'kill ${!}; shutdown_handler' KILL
trap 'kill ${!}; shutdown_handler' INT


# start service in background here
/usr/sbin/apache2ctl start

# ----------------------------------------------------------------------------------------------------
# Liferay Startup
# ----------------------------------------------------------------------------------------------------

#Replace configuration files with existing environment variables
ES_CONFIG_FILE=${LIFERAY_HOME}/osgi/configs/com.liferay.portal.search.elasticsearch.configuration.ElasticsearchConfiguration.cfg
DB_PORTAL_FILE=${LIFERAY_HOME}/tomcat-8.0.32/conf/Catalina/localhost/ROOT.xml
if [ -e $ES_CONFIG_FILE ]; then
    if [ -n "$ES_OPERATION_MODE" ]; then
        echo "Setting elastic search operation to " $ES_OPERATION_MODE
        sed -i "s/^\(operationMode\s*=\s*\).*\$/\1${ES_OPERATION_MODE}/" $ES_CONFIG_FILE
    fi
    if [ -n "$ES_TRANSPORT_ADDRESS" ]; then
        echo "Setting elastic search address to " $ES_TRANSPORT_ADDRESS
        sed -i "s/^\(transportAddresses\s*=\s*\).*\$/\1${ES_TRANSPORT_ADDRESS}/" $ES_CONFIG_FILE
    fi
fi

if [ -e $DB_PORTAL_FILE ]; then
    if [ -n $DB_URL ]; then
        DB_URL_NORM=$(echo $DB_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')
        sed -i "s/<ENTER_DATABASE_URL>/${DB_URL_NORM}/" $DB_PORTAL_FILE
    fi
    if [ -n $DB_NAME ]; then
        sed -i "s/<ENTER_DATABASE_NAME>/${DB_NAME}/" $DB_PORTAL_FILE
    fi
    if [ -n $DB_USER ]; then
        sed -i "s/<ENTER_DATABASE_SVC_ACCOUNT>/${DB_USER}/" $DB_PORTAL_FILE
    fi
    if [ -n $DB_PASSWORD ]; then
        DB_PASS_NORM=$(echo $DB_PASSWORD | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')
        sed -i "s/<ENTER_DATABASE_SVC_ACCOUNT_PWD>/${DB_PASS_NORM}/" $DB_PORTAL_FILE
    fi
fi

${TOMCAT}/bin/catalina.sh run &


pid="$!"
# wait forever
while true
do
  tail -f /dev/null & wait ${!}
done
