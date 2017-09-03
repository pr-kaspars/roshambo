package com.github.prkaspars.roshambo.config;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

public class PropertiesConfigurationSourceTest {
  private static final String VAL_1 = "foobar";
  private static final Integer VAL_2 = Integer.valueOf(123);
  private PropertiesConfigurationSource configurationSource;
  private Properties properties;

  public static class TestConfig {
    String val1;
    Integer val2;

    @Field(name = "val1")
    public void setVal1(String val1) {
      this.val1 = val1;
    }

    @Field(name = "val2")
    public void setVal2(Integer val2) {
      this.val2 = val2;
    }
  }

  @Before
  public void setUp() throws Exception {
    properties = new Properties();
    properties.setProperty("val1", VAL_1);
    properties.setProperty("val2", VAL_2.toString());

    configurationSource = new PropertiesConfigurationSource(properties);
  }

  @Test
  public void converter() throws Exception {
    Object val1 = configurationSource.converter().apply(Integer.class, "1");
    assertTrue("Value must be Integer", val1 instanceof Integer);

    Object val2 = configurationSource.converter().apply(String.class, "foo");
    assertTrue("Value must be String", val2 instanceof String);

    Object val3 = configurationSource.converter().apply(Double.class, "2.344");
    assertTrue("Value must be String", val3 instanceof String);
  }

  @Test
  public void listSetterMethods() throws Exception {
    List<Method> methods = configurationSource.listSetterMethods(TestConfig.class);
    assertEquals("List must contain 2 items", 2, methods.size());

    List<String> methodNames = methods.stream().map(method -> method.getName()).collect(toList());
    assertTrue("Must contain val1 setter", methodNames.contains("setVal1"));
    assertTrue("Must contain val2 setter", methodNames.contains("setVal2"));
  }

  @Test
  public void setter() throws Exception {
    TestConfig instance = new TestConfig();

    Method method1 = instance.getClass().getMethod("setVal1", String.class);
    configurationSource.setter(instance, properties).accept(method1);
    assertEquals("Value must be foobar", VAL_1, instance.val1);

    Method method2 = instance.getClass().getMethod("setVal2", Integer.class);
    configurationSource.setter(instance, properties).accept(method2);
    assertEquals("Value must be 123", VAL_2, instance.val2);
  }

  @Test
  public void create() throws Exception {
    TestConfig instance = configurationSource.create(TestConfig.class);
    assertTrue("Object must be TestConfig instance", instance instanceof TestConfig);
    assertEquals("Value must be foobar", VAL_1, instance.val1);
    assertEquals("Value must be 123", VAL_2, instance.val2);
  }
}
