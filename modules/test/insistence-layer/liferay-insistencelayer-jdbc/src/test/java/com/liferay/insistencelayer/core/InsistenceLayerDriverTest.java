package com.liferay.insistencelayer.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InsistenceLayerDriverTest {

  @Test
  public void AfterDeactivationEveryAccessGoesToOriginalConnections()
      throws SQLException {

    setupTwoConnectionThroughOneDriver();

    InsistenceLayerDriver.insistenceLayerConnection.increaseLayer();
    InsistenceLayerDriver.insistenceLayerConnection.decreaseLayer();

    assertEquals(connection1.getConnection(), firstRealConnection);
    assertEquals(connection2.getConnection(), secondRealConnection);

  }

  @Test
  public void EvenWhenUsingDifferentDriversTheConnectionAccessGoesThroughTheInsistenceLayerConnection()
      throws SQLException {

    when(firstDriver.connect(any(String.class), any(Properties.class)))
        .thenReturn(insistRealConnection)
        .thenReturn(firstRealConnection);

    when(secondDriver.connect(any(String.class), any(Properties.class)))
        .thenReturn(secondRealConnection);

    firstInsistDriver = new InsistenceLayerDriver(firstDriver);
    connection1 = (SwitchableConnection) firstInsistDriver
        .connect("qualquercoisa", new Properties());

    secondInsistDriver = new InsistenceLayerDriver(secondDriver);
    connection2 = (SwitchableConnection) secondInsistDriver
        .connect("qualquercoisa", new Properties());
    assertEquals(connection1.getConnection(), firstRealConnection);
    assertEquals(connection2.getConnection(), secondRealConnection);

    InsistenceLayerDriver.insistenceLayerConnection.increaseLayer();
    assertEquals(connection1.getConnection(), InsistenceLayerDriver.insistenceLayerConnection);
    assertEquals(connection2.getConnection(), InsistenceLayerDriver.insistenceLayerConnection);

  }

  @Test
  public void WhenActiveEveryConnectionAccessIsDoneThroughInsistenceLayerConnection()
      throws SQLException {

    setupTwoConnectionThroughOneDriver();

    InsistenceLayerDriver.insistenceLayerConnection.increaseLayer();
    assertEquals(connection1.getConnection(), InsistenceLayerDriver.insistenceLayerConnection);
    assertEquals(connection2.getConnection(), InsistenceLayerDriver.insistenceLayerConnection);

  }

  @After
  public void tearDown() throws SQLException {
    firstInsistDriver.stopInsistenceLayer();
  }

  private void setupTwoConnectionThroughOneDriver() throws SQLException {
    when(firstDriver.connect(any(String.class), any(Properties.class)))
        .thenReturn(insistRealConnection)
        .thenReturn(firstRealConnection)
        .thenReturn(secondRealConnection);

    firstInsistDriver = new InsistenceLayerDriver(firstDriver);
    connection1 = (SwitchableConnection) firstInsistDriver
        .connect("qualquercoisa", new Properties());
    connection2 = (SwitchableConnection) firstInsistDriver
        .connect("qualquercoisa", new Properties());
    assertEquals(connection1.getConnection(), firstRealConnection);
    assertEquals(connection2.getConnection(), secondRealConnection);
  }

  private SwitchableConnection connection1;

  private SwitchableConnection connection2;

  @Mock
  private Driver firstDriver;

  private InsistenceLayerDriver firstInsistDriver;

  @Mock
  private Connection firstRealConnection;

  @Mock
  private Connection insistRealConnection;

  @Mock
  private Driver secondDriver;

  private InsistenceLayerDriver secondInsistDriver;

  @Mock
  private Connection secondRealConnection;

}
