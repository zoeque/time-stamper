package stamper.adapter;

import io.vavr.control.Try;
import java.io.File;

public interface IFileHandleAdapter {
  /**
   * The implementation to handle files.
   * Read files and write down the certification file to the given
   * directory.
   *
   * @param file The file with an absolute path.
   * @return {@link File} instance with the result {@link Try}.
   */
  Try<File> handleFile(String file);
}
