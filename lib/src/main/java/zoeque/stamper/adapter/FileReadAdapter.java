package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.File;
import java.io.FileInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The interface class to read files.
 */
@Slf4j
@Component
public class FileReadAdapter implements IFileHandleAdapter {

  public FileReadAdapter() {
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
