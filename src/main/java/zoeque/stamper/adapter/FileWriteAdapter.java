package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The file adapter to write the timestamp file.
 */
@Slf4j
@Component
public class FileWriteAdapter implements IFileHandleAdapter {
  /**
   * Write the timestamp response as the file name + tsr extension.
   *
   * @param file target file with the absolute path.
   * @return {@link Try} with the byte array of created file.
   */
  public Try<byte[]> handleFile(Object file) {
    try {
      byte[] byteFile = (byte[]) file;
      FileOutputStream fos = new FileOutputStream(
              file + ".tsr"
      );
      fos.write(byteFile);
      return Try.success(byteFile);
    } catch (Exception e) {
      log.warn("Cannot write the timestamp response : {}", e.toString());
      return Try.failure(e);
    }
  }
}
