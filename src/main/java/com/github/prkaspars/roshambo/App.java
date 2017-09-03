package com.github.prkaspars.roshambo;

import static java.lang.Runtime.getRuntime;
import static java.util.logging.Logger.getLogger;

import com.github.prkaspars.roshambo.config.ApplicationConfiguration;
import com.github.prkaspars.roshambo.config.PropertiesConfigurationSource;
import com.github.prkaspars.roshambo.repository.ShapeRepository;
import com.github.prkaspars.roshambo.server.MainRequestHandler;
import com.github.prkaspars.roshambo.server.RequestDispatcher;
import com.github.prkaspars.roshambo.server.RequestHandler;
import com.github.prkaspars.roshambo.server.Server;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Main application class.
 */
public class App {
  private static Logger logger = getLogger(App.class.getName());
  private Server server;

  /**
   * Application entry point.
   *
   * @param args application arguments.
   */
  public static void main(String[] args) {
    String propertiesFile = "./config/application.properties";

    ApplicationConfiguration applicationConfiguration = null;
    try {
      applicationConfiguration = PropertiesConfigurationSource
            .fromFile(propertiesFile)
            .create(ApplicationConfiguration.class);
    } catch (IOException e) {
      logger.severe("Cannot create configuration");
      System.exit(5);
    }

    App app = new App(applicationConfiguration);
    getRuntime().addShutdownHook(app.getShutdownHook());
    app.launch();
  }

  /**
   * Creates new application instance.
   *
   * @param configuration the application configuration.
   */
  public App(ApplicationConfiguration configuration) {
    ShapeRepository repository = new ShapeRepository();
    try {
      Path path = Paths.get(configuration.getRepoDataFile());
      repository.setDataPath(path);
    } catch (IOException e) {
      System.exit(5);
    }

    RequestHandler requestHandler = new RequestHandler();
    requestHandler.setShapeRepository(repository);

    MainRequestHandler mainRequestHandler = new MainRequestHandler();
    mainRequestHandler.setWebRootFolder(configuration.getWebRootFolder());

    RequestDispatcher dispatcher = new RequestDispatcher();
    dispatcher.registerHandler(requestHandler);
    dispatcher.registerHandler(mainRequestHandler);

    server = new Server();
    server.setBacklog(configuration.getBacklog());
    server.setHost(configuration.getHost());
    server.setPort(configuration.getPort());
    server.setDispatcher(dispatcher);
  }

  /**
   * Returns application shutdown hook.
   *
   * @return shutdown thread.
   */
  public Thread getShutdownHook() {
    return new Thread(() -> server.close());
  }

  /**
   * Starts server.
   */
  public void launch() {
    server.start();
  }
}
