package com.github.hotire.web.resttemplate.asnyc;

import java.util.function.Supplier;
import org.springframework.http.HttpEntity;

public interface AsyncRestExchangeOperations<T> {
  Supplier<T> get(String url, HttpEntity<?> httpEntity, Object... uriVariables);
  Supplier<T> post(String url, HttpEntity<?> httpEntity, Object... uriVariables);
}
