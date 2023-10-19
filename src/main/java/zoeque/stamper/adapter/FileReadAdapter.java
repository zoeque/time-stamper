package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The interface class to read files.
 */
@Slf4j
@Component
public class FileReadAdapter implements IFileHandleAdapter {
  @Value("${zoeque.time.stamper.keystore.password}")
  String keyStorePassword;

  KeyStore keyStore;
  FileInputStream keyStoreInputStream;

  public FileReadAdapter(KeyStore keyStore,
                         FileInputStream keyStoreInputStream) {
    this.keyStore = keyStore;
    this.keyStoreInputStream = keyStoreInputStream;
  }

  /**
   * The implementation to handle files.
   * Read files and write down the certification file to the given
   * directory.
   *
   * @param file The converted byte file with an absolute path, String type.
   * @return {@link File} instance with the result {@link Try}.
   */
  public Try<byte[]> handleFile(Object file) {
    try {
      if (!(file instanceof String)) {
        throw new IllegalArgumentException("The argument must be String type");
      }
      String absoluteFilePath = (String) file;
      return Try.success(readBinaryFile(absoluteFilePath).get());
    } catch (Exception e) {
      log.warn("Cannot read and convert the file by {}", e.toString());
      return Try.failure(e);
    }
  }

  /**
   * Select alias for the key store directory.
   *
   * @return The result {@link Try} with the String type alias.
   */
  public Try<String> selectAliasForKeyStore() {
    try {
      keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
      Enumeration<String> aliases = keyStore.aliases();
      return Try.success(aliases.nextElement());
    } catch (Exception e) {
      log.warn("Cannot create alias to the key store : {}", e.toString());
      throw new IllegalStateException(e);
    }
  }

  private Try<byte[]> readBinaryFile(String file) {
    try (FileInputStream targetFileInputStream
                 = new FileInputStream(file)) {
      byte[] data = new byte[targetFileInputStream.available()];
      targetFileInputStream.read(data);
      return Try.success(data);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
