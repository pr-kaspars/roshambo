package com.github.prkaspars.roshambo.server;

import static java.util.Arrays.stream;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestDispatcher {
  private Map<Method, Object> handlers = new HashMap<>();

  /**
   * Register request handler.
   *
   * @param instance the handler instance.
   */
  public void registerHandler(Object instance) {
    for (Method method: listHandlerMethods(instance.getClass())) {
      handlers.put(method, instance);
    }
  }

  /**
   * Invokes handler method.
   *
   * @param request the request.
   * @param response the response.
   * @throws ReflectiveOperationException if method throws an exception or method can't be invoked.
   */
  public void dispatch(HttpRequest request, HttpResponse response) throws ReflectiveOperationException {
    Optional<Method> methodOptional = locate(request);
    if (!methodOptional.isPresent()) {
      response.statusCode(404);
      return;
    }

    Method method = methodOptional.get();
    HttpResponse.class.cast(method.invoke(handlers.get(method), request, response));
  }

  /**
   * Returns a list of handler methods.
   *
   * @param clazz the handler type.
   * @return the method list.
   */
  public List<Method> listHandlerMethods(Class clazz) {
    return stream(clazz.getMethods())
        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
        .collect(toList());
  }

  /**
   * Returns instance of Optional object that contains matching handler method of nothing if
   * method is not found.
   *
   * @param request the HttpRequest instance.
   * @return the optional method.
   */
  public Optional<Method> locate(HttpRequest request) {
    return handlers.keySet().stream()
        .filter(method -> {
          RequestMapping mapping = method.getAnnotation(RequestMapping.class);
          if (!mapping.method().equals(request.getMethod())) {
            return false;
          }

          return matches(mapping.path(), request.getUri());
        })
        .findFirst();
  }
}
