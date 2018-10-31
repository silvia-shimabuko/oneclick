package com.liferay.insistencelayer.core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class is the InsistenceLayer JDBC Driver. It wraps a real JDBC driver, and every connection
 * is wrapped with a SwitchableConnection linked with a single InsistenceLayerConnection While the
 * InsistenceLayerConnection is not active, the SwitchableConnection will delegate every call to the
 * real connection tha was wrapped. When the InsistenceLayerConnection is activated, the
 * SwitchableConnection will delegate every call to the InsistenceLayerConnection. The
 * InsistenceLayerConnection will prevent changes to the database, by exchanging every commit call
 * with a new savepoint, and every rollback to a rollbackToSave
 */

public class InsistenceLayerDriver extends DriverDelegate {

  public InsistenceLayerDriver(Driver driver) {
    super(driver);
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    if (insistenceLayerConnection == null || insistenceLayerConnection.isClosed()) {
      insistenceLayerConnection = new InsistenceLayerConnection(super.connect(url, info));
    }
    SwitchableConnection switchableConnection = new SwitchableConnection(super.connect(url, info));
    insistenceLayerConnection.addConnection(switchableConnection);
    return switchableConnection;
  }

  public void stopInsistenceLayer() throws SQLException {
    if (insistenceLayerConnection == null) {
      return;
    }
    insistenceLayerConnection.closeEverything();
    insistenceLayerConnection = null;
  }

  protected static volatile InsistenceLayerConnection insistenceLayerConnection;

}