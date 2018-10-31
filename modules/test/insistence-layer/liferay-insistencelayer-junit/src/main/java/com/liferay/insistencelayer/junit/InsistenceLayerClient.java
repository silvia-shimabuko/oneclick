package com.liferay.insistencelayer.junit;

import static com.liferay.insistencelayer.protocol.InsistenceLayerProtocol.DEFAULT_PORT;
import static org.apache.commons.io.IOUtils.closeQuietly;

import com.liferay.insistencelayer.protocol.InsistenceLayerProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InsistenceLayerClient {

  public InsistenceLayerClient(String host, int port) {
    this._host = host;
    this._port = port;
  }

  public InsistenceLayerClient(int port) {
    this("localhost", port);
  }


  public InsistenceLayerClient() {
    this(DEFAULT_PORT);
  }

  public void decreaseToLevel(int level) {
    this.serverCommand(InsistenceLayerProtocol.Command.DECREASE.name() + " " + level);
  }

  public int increaseLevel() {
    return this.serverCommand(InsistenceLayerProtocol.Command.INCREASE.name());
  }

  public int killServer() {
    return this.serverCommand(InsistenceLayerProtocol.Command.KILL.name());
  }

  public int serverCommand(String command) {
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    try {
      socket = new Socket(_host, _port);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      out.println(command);
      String level = in.readLine();
      return Integer.parseInt(level);

    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      closeQuietly(out);
      closeQuietly(in);
      closeQuietly(socket);
    }
  }

  private final String _host;

  private final int _port;

}
