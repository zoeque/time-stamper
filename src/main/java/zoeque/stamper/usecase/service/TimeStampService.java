package zoeque.stamper.usecase.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.TimeStampAdapter;

/**
 * The class to perform the usecase, that create request to the TSA server.
 */
@Slf4j
@Service
public class TimeStampService implements ITimeStampService {
  TimeStampAdapter timeStampAdapter;
  TimeStampRequestGenerator timeStampRequestGenerator;

  public TimeStampService(TimeStampAdapter timeStampAdapter,
                          TimeStampRequestGenerator timeStampRequestGenerator) {
    this.timeStampAdapter = timeStampAdapter;
    this.timeStampRequestGenerator = timeStampRequestGenerator;
  }

  /**
   * Create new request and send the request to the TSA server.
   * The request is created by {@link HashingFileTimeStampService} and send it
   * via {@link TimeStampAdapter}.
   *
   * @param fileBytes The converted file with a byte array.
   * @return The result {@link Try} with the TSA response.
   */
  public Try<byte[]> requestTimeStamp(byte[] fileBytes) {
    try {
      // Request the certificate
      timeStampRequestGenerator.setCertReq(true);

      // send request to the TSA server
      TimeStampRequest request
              = timeStampRequestGenerator.generate(TSPAlgorithms.SHA256, fileBytes);
      return Try.success(timeStampAdapter.sendRequest(request).get());
    } catch (Exception e) {
      log.warn("Cannot create the timestamp response file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
