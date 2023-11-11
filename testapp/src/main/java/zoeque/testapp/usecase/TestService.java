package zoeque.testapp.usecase;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import zoeque.stamper.usecase.service.TimeStampRequestService;

/**
 * Create new timestamp of given file
 */
@Service
public class TestService {
  TimeStampRequestService timeStampRequestService;

  public TestService(TimeStampRequestService timeStampRequestService) {
    this.timeStampRequestService = timeStampRequestService;
  }

  public Try<String> execute(String file) {
    try {
      Try<String> certificateTry = timeStampRequestService.createTimeStamp(file);
      return Try.success(certificateTry.get());
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
