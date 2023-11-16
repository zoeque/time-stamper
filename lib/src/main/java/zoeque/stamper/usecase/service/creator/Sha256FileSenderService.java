package zoeque.stamper.usecase.service.creator;

import io.vavr.control.Try;
import java.net.http.HttpResponse;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.TimeStampAdapter;
import zoeque.stamper.domain.model.TimeStamperConstantModel;

/**
 * The class to perform the usecase, that create request to the TSA server.
 * Given file is hashed with defined parameter
 * in {@link TimeStamperConstantModel}, SHA-256.
 * The request is sent by the autowired {@link TimeStampAdapter}.
 */
@Slf4j
@Service
public class Sha256FileSenderService extends AbstractFileSenderService {
  TimeStampAdapter timeStampAdapter;
  TimeStampRequestGenerator timeStampRequestGenerator;

  public Sha256FileSenderService(TimeStampAdapter timeStampAdapter,
                                 TimeStampRequestGenerator timeStampRequestGenerator) {
    super(timeStampRequestGenerator);
    this.timeStampAdapter = timeStampAdapter;
  }

  /**
   * Create new request and send the request to the TSA server.
   * The request is created by {@link Sha256FileSenderService} and send it
   * via {@link TimeStampAdapter}.
   *
   * @param fileBytes The converted file with a byte array.
   * @return The result {@link Try} with the TSA response.
   * The result is returned as the {@link HashMap} instance.
   * The key includes the hashed file,
   * encoded by the definition in {@link TimeStamperConstantModel}.
   * The value of the map instance has the timestamp file.
   */
  public Try<HttpResponse<byte[]>> requestTimeStamp(byte[] fileBytes) {
    try {
      TimeStampRequest request = createRequest(fileBytes, TSPAlgorithms.SHA256).get();
      return Try.success(timeStampAdapter.sendRequest(request).get());
    } catch (Exception e) {
      log.warn("Cannot create the timestamp response file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
