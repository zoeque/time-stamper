package zoeque.stamper.usecase.service.creator;

import io.vavr.control.Try;
import java.net.http.HttpResponse;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.FileReadAdapter;
import zoeque.stamper.adapter.FileWriteAdapter;

/**
 * The service class to create time stamp file.
 * The request based on the parameter is sent to the
 * {@link ITimeStampService} and create tsr file by the service.
 */
@Slf4j
@Service
public class TimeStampRequestService {
  FileReadAdapter readAdapter;
  FileWriteAdapter writeAdapter;
  BouncyCastleProvider bouncyCastleProvider;
  Sha256FileSenderService sha256FileSenderService;

  public TimeStampRequestService(FileReadAdapter readAdapter,
                                 FileWriteAdapter writeAdapter,
                                 BouncyCastleProvider bouncyCastleProvider,
                                 Sha256FileSenderService sha256FileSenderService) {
    this.readAdapter = readAdapter;
    this.writeAdapter = writeAdapter;
    this.bouncyCastleProvider = bouncyCastleProvider;
    this.sha256FileSenderService = sha256FileSenderService;
  }

  /**
   * The process to create a timestamp file.
   * This process generates the Timestamp response file,
   * hashed request file, and Timestamp token.
   *
   * @param file File with the absolute path
   * @return {@link Try} with the given argument.
   */
  public Try<String> createTimeStamp(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      byte[] targetFileBytes = readAdapter.handleFile(file).get();

      // request the timestamp to the TSA server with hashed file
      HttpResponse<byte[]> response
              = sha256FileSenderService.requestTimeStamp(targetFileBytes).get();
      Try<byte[]> writeTry = writeAdapter.handleFile(response.body());
      if (writeTry.isFailure()) {
        log.warn("Cannot write the timestamp file : {}", writeTry.getCause().toString());
        throw new IllegalStateException(writeTry.getCause());
      }

      // write timestamp token based on response
      TimeStampResponse resp = new TimeStampResponse(response.body());
      TimeStampToken token = resp.getTimeStampToken();

      Try<TimeStampToken> tokenTry = writeAdapter.handleToken(token);
      if (tokenTry.isFailure()) {
        log.warn("Cannot write token as a file!!");
        throw new IllegalStateException(tokenTry.getCause());
      }
      return Try.success(file);
    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
