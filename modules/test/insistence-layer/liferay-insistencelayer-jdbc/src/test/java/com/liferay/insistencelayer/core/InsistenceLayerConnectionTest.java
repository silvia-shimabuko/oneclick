package com.liferay.insistencelayer.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InsistenceLayerConnectionTest {

  @Before
  public void ActivationCreateSavepointAndSwitchToInsistenceLayerConnection() throws SQLException {
    when(realInsistenceConnection.setSavepoint("Manual_1")).thenReturn(savepointManual1);
    insistenceLayerConnection = new InsistenceLayerConnection(realInsistenceConnection);
    verify(realInsistenceConnection).setAutoCommit(false);
    insistenceLayerConnection.increaseLayer();
    verify(realInsistenceConnection).setSavepoint("Manual_1");
    insistenceLayerConnection.addConnection(switchableConnection);
    verify(switchableConnection).setConnection(insistenceLayerConnection);
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void CommitsAreReplacedWithSavepoints() throws SQLException {
    insistenceLayerConnection.commit();
    verify(realInsistenceConnection).setSavepoint("Commit_2");
    insistenceLayerConnection.commit();
    verify(realInsistenceConnection).setSavepoint("Commit_3");
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void DecreaseLayerToTheLastWillDeactivateInsistenceConnection() throws SQLException {
    insistenceLayerConnection.decreaseLayer();
    verify(realInsistenceConnection).rollback(savepointManual1);
    verify(switchableConnection).resetDirectConnection();
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void DecreaseLayerWillRollbackSavepoint() throws SQLException {
    Savepoint savepointManual2 = mock(Savepoint.class);
    Savepoint savepointManual3 = mock(Savepoint.class);
    when(realInsistenceConnection.setSavepoint("Manual_2")).thenReturn(savepointManual2);
    when(realInsistenceConnection.setSavepoint("Manual_3")).thenReturn(savepointManual3);
    insistenceLayerConnection.increaseLayer();
    verify(realInsistenceConnection).setSavepoint("Manual_2");
    insistenceLayerConnection.increaseLayer();
    verify(realInsistenceConnection).setSavepoint("Manual_3");
    insistenceLayerConnection.decreaseLayer();
    verify(realInsistenceConnection).rollback(savepointManual3);
    insistenceLayerConnection.decreaseLayer();
    verify(realInsistenceConnection).rollback(savepointManual2);
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void IncreaseLayerWillSetNewSavepoint() throws SQLException {
    insistenceLayerConnection.increaseLayer();
    verify(realInsistenceConnection).setSavepoint("Manual_2");
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void RollbackWillRollbackToLastSavepoint() throws SQLException {
    Savepoint savepointManual2 = mock(Savepoint.class);
    Savepoint savepointCommit3 = mock(Savepoint.class);
    when(realInsistenceConnection.setSavepoint("Manual_2")).thenReturn(savepointManual2);
    when(realInsistenceConnection.setSavepoint("Commit_3")).thenReturn(savepointCommit3);
    insistenceLayerConnection.increaseLayer();
    verify(realInsistenceConnection).setSavepoint("Manual_2");
    insistenceLayerConnection.rollback();
    verify(realInsistenceConnection).rollback(savepointManual2);
    insistenceLayerConnection.rollback();
    verify(realInsistenceConnection, times(2)).rollback(savepointManual2);
    insistenceLayerConnection.commit();
    verify(realInsistenceConnection).setSavepoint("Commit_3");
    insistenceLayerConnection.rollback();
    verify(realInsistenceConnection).rollback(savepointCommit3);
    insistenceLayerConnection.rollback();
    verify(realInsistenceConnection, times(2)).rollback(savepointCommit3);
    verifyNoMoreInteractionsWithConnections();
  }

  @Test
  public void SetAutoCommitTrueIsIgnored() throws SQLException {
    insistenceLayerConnection.setAutoCommit(true);
    verifyNoMoreInteractionsWithConnections();
  }

  private void verifyNoMoreInteractionsWithConnections() {
    verifyNoMoreInteractions(switchableConnection);
    verifyNoMoreInteractions(realInsistenceConnection);
    verifyNoMoreInteractions(realNonInsistenceConnection);
  }

  private InsistenceLayerConnection insistenceLayerConnection;

  @Mock
  private Connection realInsistenceConnection;

  @Mock
  private Connection realNonInsistenceConnection;

  @Mock
  private Savepoint savepointManual1;

  @Mock
  private SwitchableConnection switchableConnection;


}
