package com.liferay.insistencelayer.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

public class InsistenceLayerConnection extends ConnectionDelegate {

  public InsistenceLayerConnection(Connection wrapped) throws SQLException {
    super(wrapped);
    setAutoCommit(false);
  }

  public void addConnection(SwitchableConnection conn) {
    _connections.add(conn);
    if (!_savepoints.isEmpty()) {
      conn.setConnection(this);
    }
  }

  @Override
  public synchronized void close() throws SQLException {

  }

  public void closeEverything() throws SQLException {
    if (!_savepoints.isEmpty()) {
      decreaseToLevel(1);
    }
    super.close();
    for (Connection conn : _connections) {
      if (!conn.isClosed()) {
        conn.close();
      }
    }
  }

  @Override
  public synchronized void commit() throws SQLException {
    if (_savepoints.isEmpty()) {
      super.commit();
    } else {
      setSavepoint("Commit");
    }
  }

  public synchronized void decreaseLayer() {
    try {
      if (_savepoints.isEmpty()) {
        rollback();
      } else {
        rollback(_savepoints.peek().getSavepoint());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void decreaseToLevel(int level) {
    InsistenceLayerSavepoint spLvl = _savepoints.stream()
        .filter(sp -> sp.getLevel() == level)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Level " + level + " not found!"));

    try {
      rollback(spLvl.getSavepoint());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized int increaseLayer() {
    try {
      setSavepoint("Manual");
      return _savepoints.peek().getLevel();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public synchronized boolean isClosed() throws SQLException {
    return false;
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return true;
  }

  @Override
  public synchronized void rollback(Savepoint savepoint) throws SQLException {
    while (_savepoints.peek().getSavepoint() != savepoint) {
      super.rollback(_savepoints.pop().getSavepoint());
    }
    super.rollback(_savepoints.pop().getSavepoint());
    if (_savepoints.isEmpty()) {
      resetSwitchableConnections();
      System.out.println("InsistenceLayer INACTIVE");
    }
  }

  @Override
  public synchronized void rollback() throws SQLException {
    if (_savepoints.isEmpty()) {
      super.rollback();
    } else {
      super.rollback(_savepoints.peek().getSavepoint());
    }
  }

  @Override
  public synchronized void setAutoCommit(boolean autoCommit) throws SQLException {
    if (autoCommit) {
      return;
    }
    super.setAutoCommit(autoCommit);
  }

  @Override
  public synchronized Savepoint setSavepoint() throws SQLException {
    return setSavepoint("Unknown");
  }

  @Override
  public synchronized Savepoint setSavepoint(String name) throws SQLException {
    if (_savepoints.isEmpty()) {
      setInsistenceConnectionOnSwitchableConnections();
      System.out.println("InsistenceLayer ACTIVE: " + name);
    }
    int level = nextLevel();
    String realName = name + "_" + level;
    InsistenceLayerSavepoint sp = new InsistenceLayerSavepoint(super.setSavepoint(realName),
        realName, level);
    _savepoints.push(sp);
    return sp.getSavepoint();
  }

  private synchronized int nextLevel() {
    return _savepoints.size() + 1;
  }

  private void resetSwitchableConnections() {
    _connections.forEach((conn) -> conn.resetDirectConnection());
  }

  private void setInsistenceConnectionOnSwitchableConnections() {
    _connections.forEach((conn) -> conn.setConnection(this));
  }

  private final ArrayList<SwitchableConnection> _connections = new ArrayList<>();

  private Deque<InsistenceLayerSavepoint> _savepoints = new ArrayDeque<>();

}
