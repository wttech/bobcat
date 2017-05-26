package com.cognifide.qa.bb.logging.extent;

import com.aventstack.extentreports.reporter.AbstractReporter;

import java.util.List;

/**
 * Created by daniel.madejek on 2017-05-26.
 */
public interface ExtentReporterFactory {

  public List<AbstractReporter> getExtentReporters();

}
