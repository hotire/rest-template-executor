package com.github.hotire.web.resttemplate.sync;


import com.github.hotire.executor.core.common.ExecutorResponse;
import com.github.hotire.executor.core.common.Task;
import com.github.hotire.executor.core.sync.AbstractFunctionExecutor;
import com.github.hotire.web.resttemplate.common.HttpExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.http.ResponseEntity;

public class SyncFunctionExecutor<T> extends AbstractFunctionExecutor<ResponseEntity<T>, T>
  implements HttpExceptionHandler<T> {

  protected SyncFunctionExecutor(
    Task<Supplier<ResponseEntity<T>>, T> task) {
    super(task);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ExecutorResponse<T>> execute() {
    final List<ExecutorResponse<T>> executorResponses = new ArrayList<>();
    ResponseEntity<T> result;

    // execute firstTask
    try {
      result = getFirstTask().getTask().get();
    } catch (Throwable e) {
      getFirstTask().getDoOnError().accept(e);
      executorResponses.add(handleException(e));
      return executorResponses;
    }

    getFirstTask().getDoOnSuccess().accept(result.getBody());
    executorResponses.add(new ExecutorResponse<>(result.getBody(), result.getStatusCodeValue()));

    // execute tasks
    for (Task<Function<T, ResponseEntity<T>>, T> task : getTasks()) {
      try {
        result = task.getTask().apply(result.getBody());
      } catch (Throwable e) {
        task.getDoOnError().accept(e);
        executorResponses.add(handleException(e));
        break;
      }
      task.getDoOnSuccess().accept(result.getBody());
      executorResponses.add(new ExecutorResponse<>(result.getBody(), result.getStatusCodeValue()));
    }

    return executorResponses;
  }
}
