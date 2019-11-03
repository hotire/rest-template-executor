package com.github.hotire.web.resttemplate.sync;

import java.util.function.Function;
import java.util.function.Supplier;

public interface RestForEntityOperations<T, R> {
  Supplier<T> get(String url, Object... uriVariables);
  Supplier<T> post(String url, Object request, Object... uriVariables);
  Function<R, T> getFunction(String url, Object... uriVariables);
  Function<R, T> postFunction(String url, Object request, Object... uriVariables);
}
