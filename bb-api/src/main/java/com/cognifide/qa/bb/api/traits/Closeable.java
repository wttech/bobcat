package com.cognifide.qa.bb.api.traits;

/**
 * Indicates an object that thatCan be closed, e.g. window or dialog
 */
public interface Closeable {
  void close();

  boolean isClosed();
}
