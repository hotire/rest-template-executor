package com.github.hotire.web.resttemplate.asnyc;

import static java.util.stream.Collectors.toList;

import com.github.hotire.executor.core.common.AbstractSupplierExecutor;
import com.github.hotire.executor.core.common.ExecutorResponse;
import com.github.hotire.executor.core.common.Task;
import com.github.hotire.web.resttemplate.common.HttpExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;


@Slf4j
public class AsyncRestTemplateExecutor<T> extends
  AbstractSupplierExecutor<ListenableFuture<ResponseEntity<T>>, T>
  implements HttpExceptionHandler<T> {

  protected AsyncRestTemplateExecutor(
    Task<Supplier<ListenableFuture<ResponseEntity<T>>>, T> task) {
    super(task);
  }

  public static <T> AsyncRestTemplateExecutor<T> ofTask(Supplier<ListenableFuture<ResponseEntity<T>>> task) {
    return new AsyncRestTemplateExecutor<>(
      Task.of(task, throwable -> log.error("{}", throwable), result -> log.info("{}", result)));
  }

  public static <T> AsyncRestTemplateExecutor<T> ofTask(Supplier<ListenableFuture<ResponseEntity<T>>> task,
    Consumer<Throwable> doOnError) {
    return new AsyncRestTemplateExecutor<>(Task.of(task, doOnError, result -> log.info("{}", result)));
  }

  public static <T> AsyncRestTemplateExecutor<T> ofTask(Supplier<ListenableFuture<ResponseEntity<T>>> task, Consumer<Throwable> doOnError, Consumer<T> doOnSuccess) {
    return new AsyncRestTemplateExecutor<>(Task.of(task, doOnError, doOnSuccess));
  }

  @Override
  public void executeByAsync() {
    getTasks()
      .forEach(task -> task.getTask().get()
        .addCallback(
          result -> task.getDoOnSuccess().accept(result.getBody()),
          ex -> task.getDoOnError().accept(ex)
        )
      );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ExecutorResponse<T>> execute() {
    final Map<ListenableFuture<ResponseEntity<T>>,
          Task<Supplier<ListenableFuture<ResponseEntity<T>>>, T>> futureTaskMap = new HashMap<>();

    return getTasks()
      .stream()
      .map(task -> {
        ListenableFuture<ResponseEntity<T>> listenableFuture = task.getTask().get();
        futureTaskMap.put(listenableFuture, task);
        return listenableFuture;
      })
      .collect(toList())
      .stream()
      .map(listenableFuture -> {
        ResponseEntity<T> responseEntity;
        try {
          responseEntity = listenableFuture.get();
        } catch (Exception e) {
          futureTaskMap.get(listenableFuture).getDoOnError().accept(e);
          return (ExecutorResponse<T>) handleException(e);
        }
        futureTaskMap.get(listenableFuture).getDoOnSuccess().accept(responseEntity.getBody());
        return new ExecutorResponse<>(responseEntity.getBody(), responseEntity.getStatusCodeValue());
      })
      .collect(toList());
  }
}
