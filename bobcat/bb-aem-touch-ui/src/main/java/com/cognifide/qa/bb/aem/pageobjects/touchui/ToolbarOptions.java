package com.cognifide.qa.bb.aem.pageobjects.touchui;

public enum ToolbarOptions {
  EDIT("Edit"), //
  CONFIGURE("Configure"), //
  COPY("Copy"), //
  CUT("Cut"), //
  DELETE("Delete"), //
  PASTE("Paste"), //
  GROUP("Group");

  private String title;

  ToolbarOptions(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
