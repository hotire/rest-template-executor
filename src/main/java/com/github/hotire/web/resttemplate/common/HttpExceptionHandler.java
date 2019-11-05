package com.github.hotire.web.resttemplate.common;

import com.github.hotire.executor.core.common.ExecutorResponse;
import org.springframework.web.client.HttpStatusCodeException;

public interface HttpExceptionHandler<T> {
  default ExecutorResponse handleException(Throwable e) {
    if (e instanceof HttpStatusCodeException) {
      return new ExecutorResponse<T>(e, ((HttpStatusCodeException) e).getRawStatusCode());
    }
    return new ExecutorResponse<T>(e);
  }
}
