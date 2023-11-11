package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.FileOutputStream;
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
   * @param byteFile target file with the absolute path.
   * @return {@link Try} with the byte array of created file.
   */
  public Try<byte[]> handleFile(byte[] byteFile) {
    try {
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
   * @param hashedFile   The file with hashed by the SHA-256.
   * @param responseFile The timestamp response based on hashedFile..
   * @return {@link Try} with the timestamp response.
   */
  public Try<byte[]> handleFile(byte[] hashedFile, byte[] responseFile) {
    try {
      // here loops only one time
      try (FileOutputStream fosForResponse = new FileOutputStream(
              UUID.randomUUID() + ".tsr"
      );
           FileOutputStream fosForHashedFile = new FileOutputStream(
                   hashedFile + ".hash"
           )) {
        fosForHashedFile.write(hashedFile);
        fosForResponse.write(responseFile);
      } catch (RuntimeException e) {
        throw new RuntimeException("Cannot write the created files : " + e.toString());
      }
      return Try.success(responseFile);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
