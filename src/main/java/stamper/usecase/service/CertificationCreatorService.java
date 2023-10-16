package stamper.usecase.service;

import io.vavr.control.Try;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import stamper.adapter.FileReadAdapter;
import stamper.domain.factory.CertificateFactory;

@Slf4j
@Service
public class CertificationCreatorService {
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;
  FileReadAdapter readAdapter;
  BouncyCastleProvider bouncyCastleProvider;
  KeyStore keyStore;
  FileInputStream fileInputStream;
  CertificateFactory certificateFactory;

  public Try<String> createCertificate(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      String alias = readAdapter.selectAliasForKeyStore().get();

      // create certification file.
      Key key = keyStore.getKey(alias, keyStorePassword.toCharArray());
      X509Certificate certificate
              = certificateFactory.createCertificate(alias).get();

      byte[] targetFileBytes = readAdapter.handleFile(file).get();


    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }
    return null;
  }
}
