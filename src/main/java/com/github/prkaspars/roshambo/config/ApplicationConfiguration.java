package com.github.prkaspars.roshambo.config;

/**
 * Main application configuration class.
 */
public class ApplicationConfiguration {
  private Integer backlog;
  private String host;
  private Integer port;
  private String repoDataFile;
  private String webRootFolder;

  public Integer getBacklog() {
    return backlog;
  }

  @Field(name = "server.backlog")
  public void setBacklog(Integer backlog) {
    this.backlog = backlog;
  }

  public String getHost() {
    return host;
  }

  @Field(name = "server.host")
  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  @Field(name = "server.port")
  public void setPort(Integer port) {
    this.port = port;
  }

  public String getRepoDataFile() {
    return repoDataFile;
  }

  @Field(name = "repo.dataFile")
  public void setRepoDataFile(String repoDataFile) {
    this.repoDataFile = repoDataFile;
  }

  public String getWebRootFolder() {
    return webRootFolder;
  }

  @Field(name = "web.rootFolder")
  public void setWebRootFolder(String webRootFolder) {
    this.webRootFolder = webRootFolder;
  }
}
