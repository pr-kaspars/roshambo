package com.github.prkaspars.roshambo.server;

import static java.nio.file.Files.probeContentType;

import com.github.prkaspars.roshambo.server.HttpRequest.Method;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainRequestHandler {
  private String webRootFolder;

  @RequestMapping(path = "^\\/$", method = Method.GET)
  public void index(HttpRequest request, HttpResponse response) {
    Path path = Paths.get(webRootFolder + "/index.html");
    try {
      byte[] body = readFile(path);
      response.statusCode(HttpResponse.STATUS_OK)
          .putHeader("Content-Type", "text/html")
          .putHeader("Connection", "close")
          .body(body);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(path = "^(\\/[a-z]+\\/)([a-z]+)(\\.)(png|js|css)$", method = Method.GET)
  public void image(HttpRequest request, HttpResponse response) {
    Path path = Paths.get(webRootFolder + request.getUri());
    if (!Files.exists(path)) {
      response.statusCode(HttpResponse.STATUS_NOT_FOUND);
      return;
    }

    try {
      byte[] body = readFile(path);
      response.statusCode(HttpResponse.STATUS_OK)
          .putHeader("Content-Type", probeContentType(path))
          .putHeader("Connection", "close")
          .body(body);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param path
   * @return
   * @throws IOException
   */
  private byte[] readFile(Path path) throws IOException {
    try (InputStream inputStream = Files.newInputStream(path)) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
      for (int b = inputStream.read(); b >= 0; b = inputStream.read()) {
        byteArrayOutputStream.write(b);
      }
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw e;
    }
  }

  public void setWebRootFolder(String webRootFolder) {
    this.webRootFolder = webRootFolder;
  }
}
