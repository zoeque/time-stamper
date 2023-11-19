package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TimeStampToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zoeque.stamper.usecase.dto.TimeStampResponseFile;

/**
 * The file adapter to write the timestamp file.
 */
@Slf4j
@Component
public class FileWriteAdapter implements IFileHandleAdapter<byte[]> {

  String artifactDirectory;

  public FileWriteAdapter(@Value("${zoeque.time.stamper.artifact.directory}")
                          String artifactDirectory) {
    this.artifactDirectory = artifactDirectory;
  }

  /**
   * Write the timestamp response as the file name + tsr extension.
   *
   * @param responseFile The response file received from CA server.
   * @return {@link Try} with the byte array of created file.
   */
  public Try<byte[]> handleFile(TimeStampResponseFile responseFile) {
    createDirectory();
    try (FileOutputStream fos = new FileOutputStream(
            artifactDirectory + UUID.randomUUID() + ".tsr",
            false)) {
      fos.write(responseFile.file());
      return Try.success(responseFile.file());
    } catch (Exception e) {
      log.warn("Cannot write the timestamp response : {}", e.toString());
      return Try.failure(e);
    }
  }

  /**
   * Write the timestamp response and base hash file.
   * Timestamp file is named as file name + req extension.
   *
   * @param hashedFile The file with hashed by the SHA-256.
   * @return {@link Try} with the timestamp response.
   */
  public Try<byte[]> handleHashedFile(byte[] hashedFile) {
    createDirectory();
    try (FileOutputStream fos = new FileOutputStream(
            artifactDirectory + "requestFile.req",
            false)) {
      fos.write(hashedFile);
      return Try.success(hashedFile);
    } catch (Exception e) {
      log.warn("Cannot write the hashed file : {}", e.toString());
      return Try.failure(e);
    }
  }

  public Try<TimeStampToken> handleToken(TimeStampToken token) {
    createDirectory();
    try (FileOutputStream fos = new FileOutputStream(
            artifactDirectory + UUID.randomUUID() + ".tst",
            false)) {
      fos.write(token.getEncoded());
      return Try.success(token);
    } catch (Exception e) {
      log.warn("Cannot write the token : {}", e.toString());
      return Try.failure(e);
    }
  }

  private void createDirectory() {
    try {
      String pathStr = artifactDirectory;
      Path path = Paths.get(pathStr);
      Files.createDirectory(path);
    } catch (Exception e) {
      log.warn("Cannot create the artifact directory");
    }
  }
}
