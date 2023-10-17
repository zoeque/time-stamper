package stamper.adapter;

import io.vavr.control.Try;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TimeStampRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import stamper.domain.model.TimeStamperConstantModel;
import stamper.usecase.service.TimeStampService;

/**
 * The adapter class of the gateway to the TSA server.
 */
@Slf4j
@Component
public class TimeStampAdapter {
  @Value("${zoeque.time.stamper.tsa.url}")
  String tsaServerUrl;
  HttpClient httpClient;

  public TimeStampAdapter(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * Send request to the timestamp server and get its timestamp file.
   * The request is sent from {@link TimeStampService}.
   *
   * @param timeStampRequest {@link TimeStampRequest} with the hashed file.
   * @return The {@link Try} with the instance of timestamp file.
   */
  public Try<byte[]> sendRequest(TimeStampRequest timeStampRequest) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(tsaServerUrl))
              .header("Content-Type", "application/timestamp-query")
              .POST(HttpRequest.BodyPublishers.ofByteArray(timeStampRequest.getEncoded()))
              .build();
      HttpResponse<byte[]> response
              = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
      if (response.statusCode() == TimeStamperConstantModel.HTTP_OK) {
        return Try.success(response.body());
      } else {
        log.warn("TSA server returns bad response : {}", response.statusCode());
        throw new IllegalStateException();
      }
    } catch (Exception e) {
      log.warn("Cannot receive the timestamp information from TSA server : {}", e.toString());
      return Try.failure(e);
    }
  }
}
