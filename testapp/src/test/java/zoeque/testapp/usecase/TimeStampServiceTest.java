package zoeque.testapp.usecase;

import io.vavr.control.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TimeStampServiceTest {
  @Autowired
  TestService service;

  @Test
  public void attemptToCreateTimeStamp_createTimestampResponseInResourceDir() {
    String path = new ClassPathResource("TestText.txt").getPath();
    Try<String> executeTry = service.execute(path);
    Assertions.assertTrue(executeTry.isSuccess());
  }
}
