package com.cognifide.qa.bb.api.actions.basic;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.basic.open.OpenClass;
import com.cognifide.qa.bb.api.actions.basic.open.OpenObject;
import com.cognifide.qa.bb.api.traits.Openable;

/**
 * Factory class that provides a more human-readable syntax for creating Open actions.
 */
public class Open {
  public static Action the(Openable openable) {
    return new OpenObject(openable);
  }

  public static Action the(Class<? extends Openable> openable) {
    return new OpenClass(openable);
  }
}
