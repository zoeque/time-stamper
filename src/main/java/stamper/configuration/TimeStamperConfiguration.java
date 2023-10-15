package stamper.configuration;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
}
