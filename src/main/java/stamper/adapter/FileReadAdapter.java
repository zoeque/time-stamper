package stamper.adapter;

import io.vavr.control.Try;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The interface class to read files.
 */
@Service
public class FileReadAdapter implements IFileHandleAdapter {

  @Value("${zoeque.time.stamper.keystore.path}")
  String keyStorePath;
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;

  @Override
  public Try<File> handleFile() {
    return null;
  }
}
