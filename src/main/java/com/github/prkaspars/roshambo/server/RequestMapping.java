package com.github.prkaspars.roshambo.server;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.prkaspars.roshambo.server.HttpRequest.Method;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation is used to identify methods that can handle requests.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RequestMapping {

  /**
   * Request URI.
   *
   * @return The URI.
   */
  String path();

  /**
   * Request method.
   *
   * @return The method.
   */
  Method method();
}
