package com.liferay.insistencelayer.core;

import java.sql.Connection;
import java.sql.SQLException;

public class SwitchableConnection extends ConnectionDelegate {

  public SwitchableConnection(Connection conn) throws SQLException {
    super(conn);
    _directConnection = conn;
  }

  public Connection getConnection() {
    return this.wrapped;
  }

  public void resetDirectConnection() {
    this.wrapped = _directConnection;
  }

  public void setConnection(Connection conn) {
    this.wrapped = conn;
  }

  private Connection _directConnection;
}
