package zoeque.stamper.configuration;

import java.io.FileInputStream;
import java.net.http.HttpClient;
import java.security.KeyStore;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zoeque.stamper.domain.model.TimeStamperConstantModel;

/**
 * The configuration file to create Spring beans.
 */
@Slf4j
@Configuration
public class TimeStamperConfiguration {

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
  public TimeStampRequestGenerator timeStampRequestGenerator() {
    return new TimeStampRequestGenerator();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
  }
}
