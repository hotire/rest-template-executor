package com.github.hotire.web.resttemplate.sync;

import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.http.HttpEntity;

public interface RestExchangeOperations<T, R> {
  Supplier<T> get(String url, HttpEntity<?> httpEntity, Object... uriVariables);
  Supplier<T> post(String url, HttpEntity<?> httpEntity, Object... uriVariables);
  Function<R, T> getFunction(String url, HttpEntity<?> httpEntity, Object... uriVariables);
  Function<R, T> postFunction(String url, HttpEntity<?> httpEntity, Object... uriVariables);
}
