package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
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
              UUID.randomUUID() + ".tsr"
      );
      fos.write(byteFile);
      return Try.success(byteFile);
    } catch (Exception e) {
      log.warn("Cannot write the timestamp response : {}", e.toString());
      return Try.failure(e);
    }
  }

  /**
   * Write the timestamp response and base hash file.
   * Timestamp file is named as file name + tsr extension.
   *
   * @param file    The file with absolute path that is based on the timestamp request.
   * @param fileMap Map with key = hashed file and value = timestamp response.
   * @return {@link Try} with the timestamp response.
   */
  public Try<byte[]> handleFile(String file, Map<byte[], byte[]> fileMap) {
    try {
      byte[] tsResponse = null;
      // here loops only one time
      for (Map.Entry<byte[], byte[]> entry : fileMap.entrySet()) {
        try (FileOutputStream fosForResponse = new FileOutputStream(
                file + ".tsr"
        );
             FileOutputStream fosForHashedFile = new FileOutputStream(
                     file + ".hash"
             )) {
          tsResponse = entry.getValue();
          fosForHashedFile.write(entry.getKey());
          fosForResponse.write(tsResponse);
        }
      }
      return Try.success(tsResponse);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
