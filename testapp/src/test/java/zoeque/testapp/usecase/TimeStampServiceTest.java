package zoeque.testapp.usecase;

import io.vavr.control.Try;
import java.nio.file.Paths;
import java.util.List;
import org.bouncycastle.tsp.TimeStampToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zoeque.stamper.usecase.service.validator.TimeStampValidateService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TimeStampServiceTest {
  @Autowired
  TestService service;
  @Value("${zoeque.time.stamper.artifact.directory}")
  String artifactDirectory;
  @Autowired
  TimeStampValidateService validateService;

  @Test
  public void attemptToCreateTimeStamp_createTimestampResponseInResourceDir() {
    String path = Paths.get("src/test/resources/TestText.txt").toString();
    Try<String> executeTry = service.execute(path);
    Assertions.assertTrue(executeTry.isSuccess());
  }

  @Test
  public void createTimeStampResponse_whenAttemptVerify_thenReturnTokenWithValidParameters() {
    attemptToCreateTimeStamp_createTimestampResponseInResourceDir();
    Try<List<TimeStampToken>> validateTry = validateService.validate(artifactDirectory + "*.tsr");
    Assertions.assertTrue(validateTry.isSuccess());
  }
}
