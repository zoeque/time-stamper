package stamper.adapter;

import io.vavr.control.Try;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import stamper.domain.factory.CertificateFactory;

/**
 * The interface class to read files.
 */
@Slf4j
@Service
public class FileReadAdapter implements IFileHandleAdapter {
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;

  BouncyCastleProvider bouncyCastleProvider;
  KeyStore keyStore;
  FileInputStream fileInputStream;
  CertificateFactory certificateFactory;

  public FileReadAdapter(BouncyCastleProvider bouncyCastleProvider,
                         KeyStore keyStore,
                         FileInputStream fileInputStream,
                         CertificateFactory certificateFactory) {
    this.bouncyCastleProvider = bouncyCastleProvider;
    this.keyStore = keyStore;
    this.fileInputStream = fileInputStream;
    this.certificateFactory = certificateFactory;
  }

  @Override
  public Try<File> handleFile(String file) {
    try {
      Security.addProvider(bouncyCastleProvider);
      String alias = selectAliasForKeyStore().get();

      // create certification file.
      Key key = keyStore.getKey(alias, keyStorePassword.toCharArray());
      X509Certificate certificate
              = certificateFactory.createCertificate(alias).get();

    } catch (Exception e) {
      log.warn("Cannot create new timestamp file : {}", e.toString());
      return Try.failure(e);
    }

    return null;
  }

  private Try<String> selectAliasForKeyStore() {
    try {
      keyStore.load(fileInputStream, keyStorePassword.toCharArray());
      Enumeration<String> aliases = keyStore.aliases();
      return Try.success(aliases.nextElement());
    } catch (Exception e) {
      log.warn("Cannot create alias to the key store : {}", e.toString());
      throw new IllegalStateException(e);
    }
  }
}
