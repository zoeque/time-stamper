package stamper.domain.factory;

import io.vavr.control.Try;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import stamper.adapter.FileReadAdapter;

/**
 * The factory class to create new Certificate.
 */
@Slf4j
@Service
public class CertificateFactory {
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;
  KeyStore keyStore;

  public CertificateFactory(KeyStore keyStore) {
    this.keyStore = keyStore;
  }

  /**
   * The creation process for X.509 certification file.
   *
   * @param alias The alias based on the defined key store in {@link FileReadAdapter}.
   * @return Result {@link Try} with the instance of {@link X509Certificate}.
   */
  public Try<X509Certificate> createCertificate(String alias) {
    try {
      return Try.success((X509Certificate) keyStore.getCertificate(alias));
    } catch (Exception e) {
      log.warn("Cannot create new X.509 certificate : {}", e.toString());
      return Try.failure(e);
    }
  }
}
