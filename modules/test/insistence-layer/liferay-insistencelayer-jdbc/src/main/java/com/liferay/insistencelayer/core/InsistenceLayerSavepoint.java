package com.liferay.insistencelayer.core;

import java.sql.Savepoint;

public class InsistenceLayerSavepoint {

  public InsistenceLayerSavepoint(Savepoint savepoint, String name, int level) {
    this._savepoint = savepoint;
    this._name = name;
    this._level = level;
  }

  public int getLevel() {
    return _level;
  }

  public Savepoint getSavepoint() {
    return _savepoint;
  }

  @Override
  public String toString() {
    return "Savepoint(" + _name + ", " + _level + ")";
  }

  private final int _level;

  private final String _name;

  private final Savepoint _savepoint;
}
