package com.github.prkaspars.roshambo.config;

/**
 *
 */
public interface ConfigurationSource {
  <T> T create(Class<T> type) throws ReflectiveOperationException;
}
