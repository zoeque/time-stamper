package zoeque.stamper.usecase.service;

import io.vavr.control.Try;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
  @Value("${zoeque.time.stamper.hash:false}")
  boolean hashingMode;
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
   *
   * @param file File with the absolute path
   * @return {@link Try} with the given argument.
   */
  public Try<String> createTimeStamp(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      byte[] targetFileBytes = readAdapter.handleFile(file).get();

      // request the timestamp to the TSA server with hashed file
      byte[] requestTry
              = sha256FileSenderService.requestTimeStamp(targetFileBytes).get();
      Try<byte[]> writeTry = writeAdapter.handleFile(requestTry);
      if (writeTry.isFailure()) {
        log.warn("Cannot write the timestamp file : {}", writeTry.getCause().toString());
        throw new IllegalStateException(writeTry.getCause());
      }
      return Try.success(file);
    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
