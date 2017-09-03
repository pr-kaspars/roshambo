package com.github.prkaspars.roshambo.config;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Used to create configuration from a property source.
 */
public class PropertiesConfigurationSource {
  private Properties properties;

  /**
   * Returns an instance of PropertiesConfigurationSource which sources values from file.
   *
   * @param path the path to the property file.
   * @return the configuration source instance.
   * @throws IOException if an I/O error occurs.
   */
  public static PropertiesConfigurationSource fromFile(String path) throws IOException {
    Properties properties = new Properties();
    try (InputStream inputStream = newInputStream(get(path))) {
      properties.load(inputStream);
      return new PropertiesConfigurationSource(properties);
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Create an instance of PropertiesConfigurationSource.
   *
   * @param properties the property source.
   */
  public PropertiesConfigurationSource(Properties properties) {
    this.properties = properties;
  }

  /**
   * Returns an instance of the configuration class.
   *
   * @param type the configuration class type.
   * @return the configuration.
   */
  public <T> T create(Class<T> type) {
    try {
      T instance = type.newInstance();
      listSetterMethods(type).stream().forEach(setter(instance, properties));
      return instance;
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Cannot create configuration class instance");
    }
  }

  /**
   * Returns a consumer which assigns configuration option value.
   *
   * @param instance the instance to which value must be assigned.
   * @param properties the property source.
   * @return the consumer.
   */
  public Consumer<Method> setter(Object instance, Properties properties) {
    return (method) -> {
      String name = method.getAnnotation(Field.class).name();
      Class propClass = method.getParameterTypes()[0];

      if (method.getParameterCount() != 1) {
        throw new RuntimeException("Unsupported parameter count " + method.getParameterCount());
      }

      if (propClass != Integer.class && propClass != String.class) {
        throw new RuntimeException("Unsupported type " + propClass);
      }

      try {
        method.invoke(instance, converter().apply(propClass, properties.getProperty(name)));
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException("Cannot set property.", e);
      }
    };
  }

  /**
   * Returns value converter function.
   *
   * @return the function.
   */
  public BiFunction<Class, String, Object> converter() {
    return (clazz, value) -> (clazz == Integer.class) ? Integer.valueOf(value) : value;
  }

  /**
   * Returns a list of methods which have Field annotation.
   *
   * @param clazz the configuration class.
   * @return the method list.
   */
  public List<Method> listSetterMethods(Class clazz) {
    return Arrays.stream(clazz.getMethods())
        .filter(method -> method.isAnnotationPresent(Field.class))
        .collect(toList());
  }
}
