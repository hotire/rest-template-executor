package com.github.hotire.web.resttemplate.asnyc;

import java.util.function.Supplier;

public interface AsyncRestForEntityOperations<T> {
  Supplier<T> get(String url, Object... uriVariables);
  Supplier<T> post(String url, Object request, Object... uriVariables);
}
