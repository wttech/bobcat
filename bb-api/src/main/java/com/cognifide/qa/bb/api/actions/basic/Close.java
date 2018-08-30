package com.cognifide.qa.bb.api.actions.basic;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.basic.close.CloseClass;
import com.cognifide.qa.bb.api.actions.basic.close.CloseObject;
import com.cognifide.qa.bb.api.traits.Closeable;

/**
 * Factory class that provides a more human-readable syntax for creating Close actions.
 */
public class Close {

  public static Action the(Closeable closeable) {
    return new CloseObject(closeable);
  }

  public static Action the(Class<? extends Closeable> closeable) {
    return new CloseClass(closeable);
  }
}
