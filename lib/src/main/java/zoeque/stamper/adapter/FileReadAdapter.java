package zoeque.stamper.adapter;

import io.vavr.control.Try;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The interface class to read files.
 */
@Slf4j
@Component
public class FileReadAdapter implements IFileHandleAdapter<String> {
  String artifactDirectory;

  public FileReadAdapter(@Value("${zoeque.time.stamper.artifact.directory}")
                         String artifactDirectory) {
    this.artifactDirectory = artifactDirectory;
  }

  /**
   * The implementation to handle files.
   * Read files and write down the certification file to the given
   * directory.
   *
   * @param file The converted byte file with an absolute path, String type.
   * @return {@link File} instance with the result {@link Try}.
   */
  public Try<byte[]> handleFile(String file) {
    try {
      return Try.success(readBinaryFile(file).get());
    } catch (Exception e) {
      log.warn("Cannot read and convert the file by {}", e.toString());
      return Try.failure(e);
    }
  }

  /**
   * Find all response files in artifact directory.
   *
   * @param artifactDir The target directory.
   * @return The list of the tsr(response) files.
   */
  public Try<List<String>> handleResponses(String artifactDir) {
    try {
      List<String> responseFileList = new ArrayList<>();

      File directory = new File(artifactDir);
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isFile() && file.getName().endsWith(".tsr")) {
            responseFileList.add(file.toString());
          }
        }
      }
      return Try.success(responseFileList);
    } catch (Exception e) {
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
