package zoeque.stamper.usecase.service.validator;

import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.FileReadAdapter;

/**
 * The service component to verify the timestamp file.
 */
@Service
public class TimeStampValidateService
        extends AbstractTimeStampValidateService {
  public TimeStampValidateService(FileReadAdapter readAdapter) {
    super(readAdapter);
  }
}
