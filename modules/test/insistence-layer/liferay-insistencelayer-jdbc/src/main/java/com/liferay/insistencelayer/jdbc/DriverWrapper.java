package com.liferay.insistencelayer.jdbc;

import com.liferay.insistencelayer.core.InsistenceLayerDriver;
import com.liferay.insistencelayer.protocol.InsistenceLayerProtocol;
import com.liferay.portal.kernel.util.PropsUtil;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class DriverWrapper extends InsistenceLayerDriver {

  public DriverWrapper() {
    super(initDelegate());
  }

  public Connection connect(String url, Properties info) throws SQLException {

    Connection conn = super.connect(url, info);
    if (server == null) {
      startServer(parseServerPort(info));
    }
    return conn;
  }

  private static Driver initDelegate() {
    try {
      Properties properties = PropsUtil.getProperties("jdbc.driver.", true);
      String delegateClassName = properties.getProperty("delegate.class");
      Class<?> delegateClass = Class.forName(delegateClassName);
      System.out.println("Delegate " + delegateClass.toString());
      return (Driver) delegateClass.newInstance();
    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  private int parseServerPort(Properties info) {
    String serverPortProperty = (String) info.get(SERVER_PORT_PROPERTY);
    if (serverPortProperty == null) {
      return InsistenceLayerProtocol.DEFAULT_PORT;
    }
    return Integer.valueOf(serverPortProperty);
  }

  private void startServer(int serverPort) {
    server = new Server(insistenceLayerConnection, serverPort);
    new Thread(server, "Insistence Layer Server").start();
  }

  private static volatile Server server;

  private static final String SERVER_PORT_PROPERTY = "insistencelayer.server.port";

}
