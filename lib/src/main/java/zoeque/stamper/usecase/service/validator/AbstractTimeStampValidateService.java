package zoeque.stamper.usecase.service.validator;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
  BouncyCastleProvider bouncyCastleProvider;

  /**
   * The validation process that {@link TimeStampResponse} is
   * valid or not.
   * Give the pair of the timestamp response file
   * and the time stamp token with the absolute path.
   *
   * @param responsePath The response file with the absolute path.
   * @param tokenPath    The token file with the absolute path.
   * @return Return true if the response file is valid.
   */
  public Boolean validate(String responsePath,
                          String tokenPath) {
    try {
      // prepare the validation
      TimeStampResponse response
              = new TimeStampResponse(
              readAdapter.handleFile(responsePath).get());
      TimeStampToken token = response.getTimeStampToken();
      return true; // TODO
    } catch (Exception e) {
      log.warn("Cannot validate the timestamp file :{}", e.toString());
      throw new IllegalStateException(e);
    }
  }
}
