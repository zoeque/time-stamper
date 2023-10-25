package zoeque.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"zoeque.stamper", "zoeque.testapp"})
@ConfigurationPropertiesScan(basePackages = {"zoeque.stamper", "zoeque.testapp"})
public class TestApplication {
  public static void main(String... args) {
    ConfigurableApplicationContext appContext
            = SpringApplication.run(TestApplication.class, args);
  }
}
