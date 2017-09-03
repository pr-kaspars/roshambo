package com.github.prkaspars.roshambo.server;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HttpRequestTest {
  private HttpRequest httpRequest;

  @Before
  public void setUp() throws Exception {
    httpRequest = new HttpRequest();
  }

  @Test
  public void readRequestLine() throws Exception {
    final HttpRequest.Method method = HttpRequest.Method.GET;
    final String uri = "/example?foo=bar";
    final String protocolVersion = "HTTP/1.1";

    boolean result = httpRequest.readRequestLine(format("%s %s %s", method, uri, protocolVersion));
    assertTrue("Line must be request line", result);

    assertEquals("Method should be extracted", method, httpRequest.getMethod());
    assertEquals("URI should be extracted", uri, httpRequest.getUri());
    assertEquals("Protocol version should be extracted", protocolVersion, httpRequest.getProtocolVersion());
  }

  @Test
  public void readHeaderReadsHeader() throws Exception {
    final String key = "Content-Type";
    final String value = "application/x-www-form-urlencoded";

    boolean result = httpRequest.readHeader(format("%s: %s", key, value));
    assertTrue("Line must be header line", result);

    assertTrue("Header name should be extracted", httpRequest.containsHeader(key));
    assertEquals("Header value should be extracted", value, httpRequest.getHeader(key));
  }

  @Test
  public void readHeaderDoesNotReadsHeader() throws Exception {
    final String key = "Content-Type";
    final String value = "application/x-www-form-urlencoded";

    boolean result = httpRequest.readHeader(format("%s %s", key, value));
    assertFalse("Line must not be header line", result);

    assertFalse("Header should not be extracted", httpRequest.containsHeader(key));
  }
}
