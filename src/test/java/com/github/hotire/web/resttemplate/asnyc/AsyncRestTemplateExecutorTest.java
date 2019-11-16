package com.github.hotire.web.resttemplate.asnyc;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;

public class AsyncRestTemplateExecutorTest {

  private final ListenableFuture<ResponseEntity<String>> listenableFuture =
    AsyncResult.forValue(ResponseEntity.ok().body("hello"));
  private final ListenableFuture<ResponseEntity<String>> errorFuture
    = AsyncResult.forExecutionException(new RuntimeException("errorFuture"));

  @Test
  public void execute_async() {
    AsyncRestTemplateExecutor
      .ofTask(() -> listenableFuture, e -> {}, result -> assertThat(result).isEqualTo("hello"))
      .addTask(() -> errorFuture, e -> {
        assertThat(e).isInstanceOf(RuntimeException.class);
        assertThat(e.getMessage()).isEqualTo("errorFuture");
      })
      .executeByAsync();
  }

  @Test
  public void execute() {
    AsyncRestTemplateExecutor
      .ofTask(() -> listenableFuture, e -> {}, result -> assertThat(result).isEqualTo("hello"))
      .addTask(() -> errorFuture, e -> {
        e = e.getCause();
        assertThat(e).isInstanceOf(RuntimeException.class);
        assertThat(e.getMessage()).isEqualTo("errorFuture");
      })
      .execute();
  }

  @Test
  public void task() {
    AsyncRestTemplateExecutor
      .ofTask(() -> listenableFuture);

    AsyncRestTemplateExecutor
      .ofTask(() -> listenableFuture, e -> {});

    AsyncRestTemplateExecutor
      .ofTask(() -> listenableFuture, e -> {}, result -> {});
  }
}