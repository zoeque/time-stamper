package zoeque.testapp.usecase;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import zoeque.stamper.usecase.service.TimeStampCreatorService;

/**
 * Create new timestamp of given file
 */
@Service
public class TestService {
  TimeStampCreatorService timeStampCreatorService;

  public TestService(TimeStampCreatorService timeStampCreatorService) {
    this.timeStampCreatorService = timeStampCreatorService;
  }

  public Try<String> execute(String file) {
    try {
      Try<String> certificateTry = timeStampCreatorService.createCertificate(file);
      return Try.success(certificateTry.get());
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
