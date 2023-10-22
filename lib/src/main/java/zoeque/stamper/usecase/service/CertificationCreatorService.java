package zoeque.stamper.usecase.service;

import io.vavr.control.Try;
import java.security.Security;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.FileReadAdapter;
import zoeque.stamper.adapter.FileWriteAdapter;

@Slf4j
@Service
public class CertificationCreatorService {
  @Value("${zoeque.time.stamper.hash:false}")
  boolean hashingMode;
  FileReadAdapter readAdapter;
  FileWriteAdapter writeAdapter;
  BouncyCastleProvider bouncyCastleProvider;
  TimeStampService timeStampService;
  HashingFileTimeStampService hashingTimeStampService;

  public CertificationCreatorService(FileReadAdapter readAdapter,
                                     FileWriteAdapter writeAdapter,
                                     BouncyCastleProvider bouncyCastleProvider,
                                     TimeStampService timeStampService,
                                     HashingFileTimeStampService hashingTimeStampService) {
    this.readAdapter = readAdapter;
    this.writeAdapter = writeAdapter;
    this.bouncyCastleProvider = bouncyCastleProvider;
    this.timeStampService = timeStampService;
    this.hashingTimeStampService = hashingTimeStampService;
  }

  /**
   * The process to create a timestamp file.
   *
   * @param file File with the absolute path
   * @return {@link Try} with the given argument.
   */
  public Try<String> createCertificate(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      byte[] targetFileBytes = readAdapter.handleFile(file).get();

      if (hashingMode) {
        // request the timestamp to the TSA server with hashed file
        Map<byte[], byte[]> timeStampFileMap
                = hashingTimeStampService.requestTimeStamp(targetFileBytes).get();
        Try<byte[]> writeTry = writeAdapter.handleFile(timeStampFileMap);
        if (writeTry.isFailure()) {
          log.warn("Cannot write the timestamp file : {}", writeTry.getCause().toString());
          throw new IllegalStateException(writeTry.getCause());
        }
      } else {
        // request the timestamp to the TSA server
        byte[] timeStamp
                = timeStampService.requestTimeStamp(targetFileBytes).get();
        Try<byte[]> writeTry = writeAdapter.handleFile(timeStamp);
        if (writeTry.isFailure()) {
          log.warn("Cannot write the timestamp file : {}", writeTry.getCause().toString());
          throw new IllegalStateException(writeTry.getCause());
        }
      }
      return Try.success(file);
    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }
  }
}
