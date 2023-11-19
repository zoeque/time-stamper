package zoeque.stamper.adapter;

import io.vavr.control.Try;

/**
 * The interface for the file handling adapters.
 */
public interface IFileHandleAdapter<T> {
  /**
   * Handle the file to read or write to the local environment.
   *
   * @param file The file with defined type.
   * @return The byte converted file with the result {@link Try}.
   */
  Try<byte[]> handleFile(T file);
}
