package stamper.adapter;

import io.vavr.control.Try;
import java.io.File;

public interface IFileHandleAdapter {
  /**
   * The implementation to handle files.
   * Read files and write down the certification file to the given
   * directory.
   *
   * @return {@link File} instance with the result {@link Try}.
   */
  Try<File> handleFile();
}
