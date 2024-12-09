import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.support.RetryTemplate;

public class RetryTemplateTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(RetryTemplateTest.class);
  private RetryTemplate retryTemplate;

  @BeforeEach
  public void setup() {
    retryTemplate = RetryTemplate.builder()
        .maxAttempts(3)
        .retryOn(Exception.class)
        .withListener(new TestRetryListener())
        .build();
  }

  @Test
  public void retry() throws Throwable {
    retryTemplate.execute((RetryCallback<Object, Throwable>) retryContext -> {
      LOGGER.info("Retry attempt with context: {}", retryContext);
      if (retryContext.getRetryCount() == 0) {
        throw new RuntimeException("Failing first call.");
      }
      return "test";
    });

  }

  private static class TestRetryListener implements RetryListener {
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
      LOGGER.info("First try");
      return true;
    }

    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
        Throwable throwable) {
      LOGGER.info("Error when attempting.", throwable);
    }

  }
}
