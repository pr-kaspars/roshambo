package com.github.prkaspars.roshambo.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * A Thread subclass to work with socket connection.
 */
public class RequestThread extends Thread implements Closeable {
  private RequestDispatcher dispatcher;
  private Socket socket;

  /**
   * Creates an instance of RequestThread.
   *
   * @param socket the socket.
   * @param dispatcher the request dispatcher.
   */
  public RequestThread(Socket socket, RequestDispatcher dispatcher) {
    this.socket = socket;
    this.dispatcher = dispatcher;
  }

  @Override
  public void run() {
    try {
      HttpRequest request = HttpRequest.fromInputStream(socket.getInputStream());
      HttpResponse response = new HttpResponse();
      response.statusCode(HttpResponse.STATUS_OK);
      dispatcher.dispatch(request, response);
      response.send(socket.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
  }

  @Override
  public void close() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
