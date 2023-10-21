package zoeque.stamper.adapter;

import io.vavr.control.Try;

/**
 * The interface for the file handling adapters.
 */
public interface IFileHandleAdapter {
  /**
   * The method to read / write the file via File stream classes.
   *
   * @param file target file with the absolute path.
   * @return {@link Try} with byte array including the file.
   */
  Try<byte[]> handleFile(Object file);
}
