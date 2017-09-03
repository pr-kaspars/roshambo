package com.github.prkaspars.roshambo.config;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks method as property setter method.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Field {

  /**
   * The property name.
   *
   * @return the property name.
   */
  String name();
}
