package zoeque.stamper.usecase.service.creator;

import io.vavr.control.Try;
import java.security.MessageDigest;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import zoeque.stamper.domain.model.TimeStamperConstantModel;

/**
 * The common process for {@link ITimeStampService}.
 */
@Slf4j
public abstract class AbstractFileSenderService
        implements ITimeStampService {
  TimeStampRequestGenerator timeStampRequestGenerator;

  public AbstractFileSenderService(TimeStampRequestGenerator timeStampRequestGenerator) {
    this.timeStampRequestGenerator = timeStampRequestGenerator;
  }

  /**
   * Create new Request to the timestamp CA server.
   *
   * @param fileBytes The byte converted file contents.
   * @param algorithm The hashing algorithm.
   * @return The created request instance with result {@link Try}.
   */
  public Try<TimeStampRequest> createRequest(byte[] fileBytes,
                                             ASN1ObjectIdentifier algorithm) {
    try {
      // Request the certificate
      timeStampRequestGenerator.setCertReq(true);
      byte[] hashedFile = hashing(fileBytes).get();

      // send request to the TSA server
      TimeStampRequest request
              = timeStampRequestGenerator.generate(TSPAlgorithms.SHA256, hashedFile);
      return Try.success(request);
    } catch (Exception e) {
      log.warn("Cannot create new request!!");
      return Try.failure(e);
    }

  }

  private Try<byte[]> hashing(byte[] file) {
    try {
      return Try.success(MessageDigest
              .getInstance(TimeStamperConstantModel.MESSAGE_DIGEST)
              .digest(file));
    } catch (Exception e) {
      log.warn("Cannot hash the file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
