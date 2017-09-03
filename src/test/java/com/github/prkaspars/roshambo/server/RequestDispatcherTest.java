package com.github.prkaspars.roshambo.server;

import static com.github.prkaspars.roshambo.server.HttpRequest.Method.GET;
import static com.github.prkaspars.roshambo.server.HttpRequest.Method.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class RequestDispatcherTest {
  private RequestDispatcher dispatcher;

  public static class TestHandler {
    @RequestMapping(path = "/foo", method = GET)
    public void foo(HttpRequest request, HttpResponse response) {
    }
    @RequestMapping(path = "/bar", method = POST)
    public void bar(HttpRequest request, HttpResponse response) {
    }
  }

  @Before
  public void setUp() throws Exception {
    dispatcher = new RequestDispatcher();
  }

  @Test
  public void listHandlerMethods() throws Exception {
    assertEquals("", 2, dispatcher.listHandlerMethods(TestHandler.class).size());
  }

  @Test
  public void locate() throws Exception {
    HttpRequest request = mock(HttpRequest.class);

    when(request.getUri()).thenReturn("/foo");
    when(request.getMethod()).thenReturn(GET);

    dispatcher.registerHandler(new TestHandler());
    Optional<Method> methodOptional = dispatcher.locate(request);
    assertTrue("Methot must exist", methodOptional.isPresent());
    assertEquals("Method name must be foo", "foo", methodOptional.get().getName());
  }
}
