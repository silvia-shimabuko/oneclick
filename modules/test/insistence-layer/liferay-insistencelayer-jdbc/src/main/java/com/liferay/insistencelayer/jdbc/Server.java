package com.liferay.insistencelayer.jdbc;

import static org.apache.commons.io.IOUtils.closeQuietly;

import com.liferay.insistencelayer.core.InsistenceLayerConnection;
import com.liferay.insistencelayer.protocol.InsistenceLayerProtocol.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server implements Runnable {

  public Server(InsistenceLayerConnection connection, int port) {
    this._port = port;
    this._connection = connection;
    _pool = Executors.newSingleThreadExecutor();
  }

  @Override
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(_port)) {
      acceptConnections(serverSocket);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      _pool.shutdown();
    }
  }

  private void acceptConnections(ServerSocket serverSocket) throws IOException {
    while (_continueAcceptConnections) {
      Socket socket = serverSocket.accept();
      _pool.execute(new ConnectionHandler(socket, _connection, this));
    }
  }

  private static class ConnectionHandler implements Runnable {

    public ConnectionHandler(Socket socket, InsistenceLayerConnection connection, Server server) {
      this._socket = socket;
      this._connection = connection;
      this._server = server;
    }

    public void run() {
      try {
        processRequest();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        closeQuietly(_socket);
      }
    }

    private void processRequest() throws IOException {
      BufferedReader reader = null;
      PrintWriter writer = null;
      try {
        reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        writer = new PrintWriter(_socket.getOutputStream(), true);
        String line = reader.readLine();
        String[] args = line.split(" ");
        Command command = Command.valueOf(args[0]);
        switch (command) {
          case DECREASE: {
            _connection.decreaseToLevel(Integer.parseInt(args[1]));
            writer.println("0");
            break;
          }
          case INCREASE: {
            int level = _connection.increaseLayer();
            writer.println(level);
            break;
          }
          case KILL: {
            _server._continueAcceptConnections = false;
            killCommand();
            writer.println(1);
            break;
          }
          default: {

          }
        }

      } finally {
        closeQuietly(reader);
        closeQuietly(writer);
      }
    }

    private void killCommand() {
      PrintWriter out = null;
      BufferedReader in = null;
      try (Socket socket = new Socket("localhost", _server._port)) {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(Command.KILL.name());
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        closeQuietly(out);
        closeQuietly(in);
      }
    }

    private final InsistenceLayerConnection _connection;

    private final Socket _socket;

    private final Server _server;

  }

  private final InsistenceLayerConnection _connection;

  private final ExecutorService _pool;

  private final int _port;

  private boolean _continueAcceptConnections = true;

}
