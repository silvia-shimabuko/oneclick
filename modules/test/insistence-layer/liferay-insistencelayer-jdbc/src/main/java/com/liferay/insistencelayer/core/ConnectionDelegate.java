package com.liferay.insistencelayer.core;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionDelegate implements Connection {

  public ConnectionDelegate(Connection wrapped) throws SQLException {
    this.wrapped = wrapped;
  }

  public synchronized void abort(Executor executor) throws SQLException {
    wrapped.abort(executor);
  }

  public synchronized void clearWarnings() throws SQLException {
    wrapped.clearWarnings();
  }

  public synchronized void close() throws SQLException {
    wrapped.close();
  }

  public synchronized void commit() throws SQLException {
    wrapped.commit();
  }

  public synchronized Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return wrapped.createArrayOf(typeName, elements);
  }

  public synchronized Blob createBlob() throws SQLException {
    return wrapped.createBlob();
  }

  public synchronized Clob createClob() throws SQLException {
    return wrapped.createClob();
  }

  public synchronized NClob createNClob() throws SQLException {
    return wrapped.createNClob();
  }

  public synchronized SQLXML createSQLXML() throws SQLException {
    return wrapped.createSQLXML();
  }

  public synchronized Statement createStatement() throws SQLException {
    return wrapped.createStatement();
  }

  public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return wrapped.createStatement(resultSetType, resultSetConcurrency);
  }

  public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    return wrapped.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public synchronized Struct createStruct(String typeName, Object[] attributes)
      throws SQLException {
    return wrapped.createStruct(typeName, attributes);
  }

  public synchronized boolean getAutoCommit() throws SQLException {
    return wrapped.getAutoCommit();
  }

  public synchronized String getCatalog() throws SQLException {
    return wrapped.getCatalog();
  }

  public synchronized String getClientInfo(String name) throws SQLException {
    return wrapped.getClientInfo(name);
  }

  public synchronized Properties getClientInfo() throws SQLException {
    return wrapped.getClientInfo();
  }

  public synchronized int getHoldability() throws SQLException {
    return wrapped.getHoldability();
  }

  public synchronized DatabaseMetaData getMetaData() throws SQLException {
    return wrapped.getMetaData();
  }

  public synchronized int getNetworkTimeout() throws SQLException {
    return wrapped.getNetworkTimeout();
  }

  public synchronized String getSchema() throws SQLException {
    return wrapped.getSchema();
  }

  public synchronized int getTransactionIsolation() throws SQLException {
    return wrapped.getTransactionIsolation();
  }

  public synchronized Map<String, Class<?>> getTypeMap() throws SQLException {
    return wrapped.getTypeMap();
  }

  public synchronized SQLWarning getWarnings() throws SQLException {
    return wrapped.getWarnings();
  }

  public synchronized boolean isClosed() throws SQLException {
    return wrapped.isClosed();
  }

  public synchronized boolean isReadOnly() throws SQLException {
    return wrapped.isReadOnly();
  }

  public synchronized boolean isValid(int timeout) throws SQLException {
    return wrapped.isValid(timeout);
  }

  public synchronized boolean isWrapperFor(Class<?> iface) throws SQLException {
    return wrapped.isWrapperFor(iface);
  }

  public synchronized String nativeSQL(String sql) throws SQLException {
    return wrapped.nativeSQL(sql);
  }

  public synchronized CallableStatement prepareCall(String sql) throws SQLException {
    return wrapped.prepareCall(sql);
  }

  public synchronized CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  public synchronized CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public synchronized PreparedStatement prepareStatement(String sql) throws SQLException {
    return wrapped.prepareStatement(sql);
  }

  public synchronized PreparedStatement prepareStatement(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  public synchronized PreparedStatement prepareStatement(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public synchronized PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
      throws SQLException {
    return wrapped.prepareStatement(sql, autoGeneratedKeys);
  }

  public synchronized PreparedStatement prepareStatement(String sql, int[] columnIndexes)
      throws SQLException {
    return wrapped.prepareStatement(sql, columnIndexes);
  }

  public synchronized PreparedStatement prepareStatement(String sql, String[] columnNames)
      throws SQLException {
    return wrapped.prepareStatement(sql, columnNames);
  }

  public synchronized void releaseSavepoint(Savepoint savepoint) throws SQLException {
    wrapped.releaseSavepoint(savepoint);
  }

  public synchronized void rollback() throws SQLException {
    wrapped.rollback();
  }

  public synchronized void rollback(Savepoint savepoint) throws SQLException {
    wrapped.rollback(savepoint);
  }

  public synchronized void setAutoCommit(boolean autoCommit) throws SQLException {
    wrapped.setAutoCommit(autoCommit);
  }

  public synchronized void setCatalog(String catalog) throws SQLException {
    wrapped.setCatalog(catalog);
  }

  public synchronized void setClientInfo(String name, String value) throws SQLClientInfoException {
    wrapped.setClientInfo(name, value);
  }

  public synchronized void setClientInfo(Properties properties) throws SQLClientInfoException {
    wrapped.setClientInfo(properties);
  }

  public synchronized void setHoldability(int holdability) throws SQLException {
    wrapped.setHoldability(holdability);
  }

  public synchronized void setNetworkTimeout(Executor executor, int milliseconds)
      throws SQLException {
    wrapped.setNetworkTimeout(executor, milliseconds);
  }

  public synchronized void setReadOnly(boolean readOnly) throws SQLException {
    wrapped.setReadOnly(readOnly);
  }

  public synchronized Savepoint setSavepoint() throws SQLException {
    return wrapped.setSavepoint();
  }

  public synchronized Savepoint setSavepoint(String name) throws SQLException {
    return wrapped.setSavepoint(name);
  }

  public synchronized void setSchema(String schema) throws SQLException {
    wrapped.setSchema(schema);
  }

  public synchronized void setTransactionIsolation(int level) throws SQLException {
    wrapped.setTransactionIsolation(level);
  }

  public synchronized void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    wrapped.setTypeMap(map);
  }

  public synchronized <T> T unwrap(Class<T> iface) throws SQLException {
    return wrapped.unwrap(iface);
  }

  protected Connection wrapped;
}
