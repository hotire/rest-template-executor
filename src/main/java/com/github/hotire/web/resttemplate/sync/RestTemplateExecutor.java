package com.github.hotire.web.resttemplate.sync;



import com.github.hotire.executor.core.common.Task;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class RestTemplateExecutor {

  private RestTemplateExecutor(){}

  public static <T> SyncSupplierExecutor<T> ofTask(Supplier<ResponseEntity<T>> task) {
    return new SyncSupplierExecutor<>(
      Task.of(task, throwable -> log.error("{}", throwable), result -> log.info("{}", result)));
  }

  public static <T> SyncSupplierExecutor<T> ofTask(Supplier<ResponseEntity<T>> task, Consumer<Throwable> doOnError) {
    return new SyncSupplierExecutor<>(Task.of(task, doOnError, result -> log.info("{}", result)));
  }

  public static <T> SyncSupplierExecutor<T> ofTask(Supplier<ResponseEntity<T>> task, Consumer<Throwable> doOnError, Consumer<T> doOnSuccess) {
    return new SyncSupplierExecutor<>(Task.of(task, doOnError, doOnSuccess));
  }

  public static <T> SyncFunctionExecutor<T> ofFunctionTask(Supplier<ResponseEntity<T>> task) {
    return new SyncFunctionExecutor<>(Task.of(task, throwable -> log.error("{}", throwable), result -> log.info("{}", result)));
  }

  public static <T> SyncFunctionExecutor<T> ofFunctionTask(Supplier<ResponseEntity<T>> task, Consumer<Throwable> doOnError) {
    return new SyncFunctionExecutor<>(Task.of(task, doOnError, result -> log.info("{}", result)));
  }

  public static <T> SyncFunctionExecutor<T> ofFunctionTask(Supplier<ResponseEntity<T>> task, Consumer<Throwable> doOnError, Consumer<T> doOnSuccess) {
    return new SyncFunctionExecutor<>(Task.of(task, doOnError, doOnSuccess));
  }
}
