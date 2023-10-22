import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"zoeque"})
@ComponentScan(basePackages = {"zoeque"})
@ConfigurationPropertiesScan(basePackages = {"zoeque"})
public class TestApplication {
  public static void main(String... args) {
    ConfigurableApplicationContext appContext
            = SpringApplication.run(TestApplication.class, args);
  }
}
