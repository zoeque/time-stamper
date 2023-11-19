package zoeque.stamper.usecase.service.validator;

import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import zoeque.stamper.adapter.FileReadAdapter;

/**
 * The abstract class to validate the pair of
 * the timestamp response and its token.
 */
@Slf4j
public abstract class AbstractTimeStampValidateService {

  FileReadAdapter readAdapter;

  public AbstractTimeStampValidateService(FileReadAdapter readAdapter) {
    this.readAdapter = readAdapter;
  }

  /**
   * The validation process that {@link TimeStampResponse} is
   * valid or not.
   * Give the pair of the timestamp response file
   * and the time stamp token with the absolute path.
   *
   * @param responsePath The response file with the absolute path.
   * @return Return Instance of {@link TimeStampToken} if the response file is valid.
   */
  public Try<List<TimeStampToken>> validate(String responsePath) {
    try {
      // prepare the validation
      Try<List<String>> responses
              = readAdapter.handleResponses(responsePath);
      List<TimeStampToken> tokens = new ArrayList<>();
      for (String responseDir : responses.get()) {
        TimeStampResponse timeStampResponse
                = new TimeStampResponse(readAdapter.handleFile(responseDir).get());
        TimeStampToken token = timeStampResponse.getTimeStampToken();
        tokens.add(token);
        if (token == null) {
          throw new IllegalArgumentException("Response is invalid!!");
        }
      }
      return Try.success(tokens);
    } catch (Exception e) {
      log.warn("Cannot validate the timestamp file :{}", e.toString());
      return Try.failure(e);
    }
  }
}
