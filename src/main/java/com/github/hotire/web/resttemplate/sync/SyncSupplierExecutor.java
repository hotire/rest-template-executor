package com.github.hotire.web.resttemplate.sync;



import com.github.hotire.executor.core.common.AbstractSupplierExecutor;
import com.github.hotire.executor.core.common.ExecutorResponse;
import com.github.hotire.executor.core.common.Task;
import com.github.hotire.web.resttemplate.common.HttpExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.http.ResponseEntity;

public class SyncSupplierExecutor<T> extends AbstractSupplierExecutor<ResponseEntity<T>, T>
  implements HttpExceptionHandler<T> {

  protected SyncSupplierExecutor(
    Task<Supplier<ResponseEntity<T>>, T> task) {
    super(task);
  }

  @Override
  public void executeByAsync() {
    throw new UnsupportedOperationException("Unsupported Async Operation");
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ExecutorResponse<T>> execute() {
    final List<ExecutorResponse<T>> executorResponses = new ArrayList<>();

    for (Task<Supplier<ResponseEntity<T>>, T> task : getTasks()) {
      ResponseEntity<T> result;
      try {
        result = task.getTask().get();
      } catch (Exception e) {
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