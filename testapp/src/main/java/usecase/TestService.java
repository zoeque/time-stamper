package usecase;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import zoeque.stamper.usecase.service.CertificationCreatorService;

/**
 * Create new timestamp of given file
 */
@Service
public class TestService {
  CertificationCreatorService certificationCreatorService;

  public TestService(CertificationCreatorService certificationCreatorService) {
    this.certificationCreatorService = certificationCreatorService;
  }

  public Try<String> execute(String file) {
    try {
      Try<String> certificateTry = certificationCreatorService.createCertificate(file);
      return Try.success(certificateTry.get());
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
