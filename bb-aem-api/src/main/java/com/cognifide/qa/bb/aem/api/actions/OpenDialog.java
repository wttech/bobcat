package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.aem.api.actions.opendialog.OpenDialogForComponent;
import com.cognifide.qa.bb.api.actions.Action;

/**
 * Factory class to provide a human-readable API to create OpenDialog actions
 */
public class OpenDialog {

  public static Action forComponent(Class component) {
    return new OpenDialogForComponent(component);
  }
}
