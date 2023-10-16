package stamper.configuration;

import java.io.FileInputStream;
import java.security.KeyStore;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stamper.domain.model.TimeStamperConstantModel;

/**
 * The configuration file to create Spring beans.
 */
@Slf4j
@Configuration
public class TimeStamperConfiguration {
  @Value("${zoeque.time.stamper.keystore.path}")
  String keyStorePath;

  @Bean
  public BouncyCastleProvider bouncyCastleProvider() {
    return new BouncyCastleProvider();
  }

  /**
   * Create {@link KeyStore} bean.
   * The format of encoding is defined in {@link TimeStamperConstantModel}.
   *
   * @return The bean of keyStore.
   */
  @Bean
  public KeyStore keyStore() {
    try {
      return KeyStore.getInstance(TimeStamperConstantModel.ENCODING_FORMAT);
    } catch (Exception e) {
      log.error("Cannot create new instance of KeyStore : {}", e.toString());
      throw new IllegalStateException(e);
    }
  }

  @Bean
  public FileInputStream fileInputStream() {
    try {
      return new FileInputStream(keyStorePath);
    } catch (Exception e) {
      log.error("Cannot create new instance of FileInputStream : {}", e.toString());
      throw new IllegalStateException(e);
    }
  }
}
