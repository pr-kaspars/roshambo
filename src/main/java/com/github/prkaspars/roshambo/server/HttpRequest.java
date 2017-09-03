package com.github.prkaspars.roshambo.server;

import static java.util.Arrays.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents HTTP request with request line, headers and body.
 */
public class HttpRequest {

  /**
   * Enum that represents HTTP method.
   */
  public enum Method {
    GET, POST
  }

  /**
   * Regular expression to match http header.
   */
  private static Pattern PATTERN_HEADER = Pattern.compile("^(.+):(.+)$");

  /**
   * Regular expression to match request line.
   */
  private static Pattern PATTERN_REQUEST_LINE = Pattern.compile("^([A-Z]+)(\\s+)(.+)(\\s+)(.+)$");

  private static Pattern PATTERN_URI = Pattern.compile("^(.*)(\\?)(.*)$");

  private Map<String, String> headers = new HashMap<>();
  private Map<String, String> query = new HashMap<>();
  private Method method;
  private String uri;
  private String protocolVersion;
  private String body;

  /**
   * Returns an instance of HttpRequest constructed from input stream.
   *
   * @param inputStream The request InputStream.
   * @return The HttpRequest instance.
   * @throws IOException If an I/O error occurs.
   */
  public static HttpRequest fromInputStream(InputStream inputStream) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

    HttpRequest httpRequest = new HttpRequest();

    String line = in.readLine();
    httpRequest.readRequestLine(line);

    while((line = in.readLine()) != null && !line.equals("")) {
      httpRequest.readHeader(line);
    }

    return httpRequest;
  }

  /**
   * Parses argument, extracts HTTP header and returns true if line was http header.
   *
   * @param line The header line.
   * @return True if line is header.
   */
  public boolean readHeader(String line) {
    Matcher headerMatcher = PATTERN_HEADER.matcher(line);
    if (!headerMatcher.matches()) {
      return false;
    }

    headers.put(headerMatcher.group(1).trim(), headerMatcher.group(2).trim());
    return true;
  }

  /**
   * Parses argument, extracts request line and returns true if line is request line.
   *
   * @param line Request line.
   * @return True if line is request line.
   */
  public boolean readRequestLine(String line) {
    Matcher requestLineMatcher = PATTERN_REQUEST_LINE.matcher(line);
    if (!requestLineMatcher.matches()) {
      return false;
    }

    method = Method.valueOf(requestLineMatcher.group(1));
    uri = requestLineMatcher.group(3);
    protocolVersion = requestLineMatcher.group(5);

    if (PATTERN_URI.matcher(uri).matches()) {
      query = stream(URI.create(uri).getQuery().split("&"))
          .map(param -> param.split("="))
          .map(parts -> new SimpleEntry<>(parts[0], parts[1]))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    return true;
  }

  /**
   * Assigns value to request body field;
   *
   * @param line Request line.
   */
  public void readBody(String line) {
    body = line;
  }

  public boolean containsHeader(String key) {
    return headers.containsKey(key);
  }

  public Integer getContentLength() {
    if (!headers.containsKey("Content-Length")) {
      return null;
    }

    return Integer.valueOf(headers.get("Content-Length"));
  }

  public String getHeader(String key) {
    return headers.get(key);
  }

  public String getParameter(String name) {
    return query.get(name);
  }

  public boolean containsParameter(String name) {
    return query != null && query.containsKey(name);
  }

  public Method getMethod() {
    return method;
  }

  public String getUri() {
    return uri;
  }

  public String getProtocolVersion() {
    return protocolVersion;
  }

  public String getBody() {
    return body;
  }
}
