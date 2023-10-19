package zoeque.stamper.usecase.service;

import io.vavr.control.Try;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zoeque.stamper.adapter.FileReadAdapter;
import zoeque.stamper.adapter.FileWriteAdapter;
import zoeque.stamper.domain.factory.CertificateFactory;

@Slf4j
@Service
public class CertificationCreatorService {
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;
  @Value("${zoeque.time.stamper.hash:false}")
  boolean hashingMode;
  FileReadAdapter readAdapter;
  FileWriteAdapter writeAdapter;
  BouncyCastleProvider bouncyCastleProvider;
  KeyStore keyStore;
  CertificateFactory certificateFactory;
  TimeStampService timeStampService;
  HashingFileTimeStampService hashingTimeStampService;

  public CertificationCreatorService(FileReadAdapter readAdapter,
                                     FileWriteAdapter writeAdapter,
                                     BouncyCastleProvider bouncyCastleProvider,
                                     KeyStore keyStore,
                                     CertificateFactory certificateFactory,
                                     TimeStampService timeStampService,
                                     HashingFileTimeStampService hashingTimeStampService) {
    this.readAdapter = readAdapter;
    this.writeAdapter = writeAdapter;
    this.bouncyCastleProvider = bouncyCastleProvider;
    this.keyStore = keyStore;
    this.certificateFactory = certificateFactory;
    this.timeStampService = timeStampService;
    this.hashingTimeStampService = hashingTimeStampService;
  }

  public Try<String> createCertificate(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      String alias = readAdapter.selectAliasForKeyStore().get();

      // create certification file.
      Key key = keyStore.getKey(alias, keyStorePassword.toCharArray());
      X509Certificate certificate
              = certificateFactory.createCertificate(alias).get();

      byte[] targetFileBytes = readAdapter.handleFile(file).get();

      if (hashingMode) {
        // request the timestamp to the TSA server with hashed file
        Map<byte[], byte[]> timeStampFileMap
                = hashingTimeStampService.requestTimeStamp(targetFileBytes).get();
      } else {
        byte[] timeStamp
                = timeStampService.requestTimeStamp(targetFileBytes).get();
        writeAdapter.handleFile(timeStamp);
      }
    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }
    return null;
  }
}
