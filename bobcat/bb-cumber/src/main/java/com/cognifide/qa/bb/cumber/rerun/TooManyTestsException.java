package com.cognifide.qa.bb.cumber.rerun;

public class TooManyTestsException extends Exception {
  public TooManyTestsException() {
    super("There was too many tests failures.");
  }
}
