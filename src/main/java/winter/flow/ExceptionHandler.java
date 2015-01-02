package winter.flow;

import java.util.concurrent.ExecutionException;

/**
* Created by aviadbendov on 1/1/15.
*/
public interface ExceptionHandler {
    void handleException(RuntimeException ex);
    void handleException(ExecutionException ex);
}
