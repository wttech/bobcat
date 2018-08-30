package com.cognifide.qa.bb.api.misc;

import com.cognifide.qa.bb.api.traits.Closeable;
import com.cognifide.qa.bb.api.traits.Openable;

public class DummyWindow implements Closeable, Openable {
  @Override
  public void close() {
    System.out.println("Closing window");
  }

  @Override
  public boolean isClosed() {
    System.out.println("Verifying that window is closed");
    return true;
  }

  @Override
  public void open() {
    System.out.println("Opening window");
  }

  @Override
  public boolean isOpened() {
    System.out.println("Verifying that window is opened");
    return true;
  }
}
