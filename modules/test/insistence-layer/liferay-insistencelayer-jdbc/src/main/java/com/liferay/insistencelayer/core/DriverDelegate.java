package com.liferay.insistencelayer.core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverDelegate implements Driver {

  public DriverDelegate(Driver wrapper) {
    this._wrapper = wrapper;
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return _wrapper.acceptsURL(url);
  }

  public Connection connect(String url, Properties info) throws SQLException {
    return _wrapper.connect(url, info);
  }

  @Override
  public int getMajorVersion() {
    return _wrapper.getMajorVersion();
  }

  @Override
  public int getMinorVersion() {
    return _wrapper.getMinorVersion();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return _wrapper.getParentLogger();
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return _wrapper.getPropertyInfo(url, info);
  }

  @Override
  public boolean jdbcCompliant() {
    return _wrapper.jdbcCompliant();
  }

  private Driver _wrapper;

}
