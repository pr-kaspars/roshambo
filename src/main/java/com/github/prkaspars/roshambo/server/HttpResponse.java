package com.github.prkaspars.roshambo.server;

import static java.lang.String.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Object that represents HTTP response.
 */
public class HttpResponse {
  public static final int STATUS_OK = 200;
  public static final  int STATUS_NOT_FOUND = 404;
  private static final String line = "%s\r\n";
  private static final String header_line = "%s: %s\r\n";

  private Map<String, String> headers = new HashMap<>();
  private int statusCode = STATUS_OK;
  private byte[] body;

  /**
   * Sets response status code.
   *
   * @param statusCode response status code.
   * @return the response, not null.
   */
  public HttpResponse statusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Puts request header key-value pair.
   *
   * @param name the header name.
   * @param value the header value.
   * @return the response, not null.
   */
  public HttpResponse putHeader(String name, String value) {
    this.headers.put(name, value);
    return this;
  }

  /**
   * Sets response content.
   *
   * @param body the response content.
   * @return the response, not null.
   */
  public HttpResponse body(byte[] body) {
    this.body = body;
    return this;
  }

  public String getStatusLine() {
    switch (statusCode) {
      case 200:
        return "HTTP/1.1 200 OK";
      case 404:
        return "HTTP/1.1 404 Not Found";
      default:
        return null;
    }
  }

  public void send(OutputStream outputStream) throws IOException {
    outputStream.write(format(line, getStatusLine()).getBytes());
    outputStream.flush();

    for (Entry<String, String> entry: headers.entrySet()) {
      outputStream.write(format(header_line, entry.getKey(), entry.getValue()).getBytes());
    }

    outputStream.write(format(line, "").getBytes());
    outputStream.flush();

    if (body == null) {
      return;
    }

    outputStream.write(body);
    outputStream.flush();
  }
}
