package com.github.prkaspars.roshambo.server;

import static java.lang.String.format;
import static java.net.InetAddress.getByName;
import static java.util.Arrays.stream;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.joining;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * MVP web server.
 */
public class Server extends Thread implements Closeable {
  private static Logger logger = getLogger(Server.class.getName());

  private int backlog = 50;
  private int port = 8080;
  private String host = "localhost";

  private ServerSocket serverSocket;
  private List<Closeable> connections = new ArrayList<>();
  private RequestDispatcher dispatcher;

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(port, backlog, getByName(host));
      logger.info(format("Server started %s:%d", host, port));
      listen(serverSocket);
    } catch(Exception e) {
      String stackTrace = stream(e.getStackTrace())
          .map(StackTraceElement::toString)
          .collect(joining("\n"));

      logger.severe(" " + stackTrace);
    } finally {
      close();
    }
  }

  void listen(ServerSocket serverSocket) throws IOException {
    while(true) {
      Socket socket = serverSocket.accept();
      RequestThread thread = new RequestThread(socket, dispatcher);
      connections.add(thread);
      thread.start();
    }
  }

  @Override
  public void close() {
    for (Closeable connection: connections) {
      try {
        connection.close();
      } catch (IOException e) {

      }
    }

    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void setBacklog(int backlog) {
    this.backlog = backlog;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setDispatcher(RequestDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }
}
