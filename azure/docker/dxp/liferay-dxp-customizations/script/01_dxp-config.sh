#!/usr/bin/env bash

  if [ -f $ENTRYPOINT_DIR/script/liferay/cluster-properties.xml ]; then
    echo "copying cluster-properties.xml"
    cp -f $ENTRYPOINT_DIR/script/liferay/cluster-properties.xml $LIFERAY_HOME/
  fi

  if [ -d $ENTRYPOINT_DIR/script/liferay/osgi ]; then
    echo "copying osgi configuration"
    cp -rf $ENTRYPOINT_DIR/script/liferay/osgi/* $LIFERAY_HOME/osgi/
  fi

  if [ -d $ENTRYPOINT_DIR/script/liferay/tomcat ]; then
    echo "copying tomcat configuration"
    cp -rf $ENTRYPOINT_DIR/script/liferay/tomcat/* $LIFERAY_HOME/tomcat/
  fi

  if [ "ROOT" != "ROOT" ]; then
    echo "renaming ROOT -> ROOT"
    mv $LIFERAY_HOME/tomcat/webapps/ROOT $LIFERAY_HOME/tomcat/webapps/ROOT
    echo "deleting ROOT.xml"
    rm -f $LIFERAY_HOME/tomcat/conf/Catalina/localhost/ROOT.xml
  fi

  (
    IFS='
'
    REMOVE_LPKGS='Liferay Connected Services Client.lpkg
Liferay Documentum Connector.lpkg
Liferay OAuth Provider.lpkg
Liferay Sharepoint Connector.lpkg
Liferay Sync Connector.lpkg'
    if [ -n "$REMOVE_LPKGS" ]; then
      echo "deleting extra lpkg's"
      for LPKG in $REMOVE_LPKGS; do
        rm -fv "$LIFERAY_HOME/osgi/marketplace/$LPKG"
      done
    fi
  )

