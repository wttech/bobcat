package com.cognifide.qa.bb.api.traits;

/**
 * Indicates an element that thatCan be opened, e.g. window, dialog or a page.
 */
public interface Openable {
  void open(); //todo think about template pattern here - these objects should auto-validate if they were opened

  boolean isOpened(); //todo think if this should be some sort of condition instead of boolean, so it thatCan be dynamically validated
}
