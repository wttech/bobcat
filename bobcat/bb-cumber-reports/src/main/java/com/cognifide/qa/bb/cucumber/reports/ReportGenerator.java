/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.cucumber.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

import com.cognifide.qa.bb.cucumber.reports.configuration.ConfigurationBuilder;

import net.masterthought.cucumber.ReportBuilder;

/**
 * Created by daniel.madejek on 2016-08-29.
 */
public class ReportGenerator {

  private final static String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.json";

  public void generateReport() {

    File reportOutputDirectory = new File("./target");

    String[] jsonFiles1 = findJsonFiles(reportOutputDirectory);
    List<String> jsonFiles = fullPathToJsonFiles(jsonFiles1, reportOutputDirectory);

    ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, ConfigurationBuilder.build());
    reportBuilder.generateReports();
  }

  private String[] findJsonFiles(File targetDirectory) {
    DirectoryScanner scanner = new DirectoryScanner();

    scanner.setIncludes(new String[] {DEFAULT_FILE_INCLUDE_PATTERN});

    scanner.setBasedir(targetDirectory);
    scanner.scan();
    return scanner.getIncludedFiles();
  }

  private List<String> fullPathToJsonFiles(String[] jsonFiles, File targetBuildDirectory) {
    List<String> fullPathList = new ArrayList<String>();
    for (String file : jsonFiles) {
      fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
      System.out.println(new File(targetBuildDirectory, file).getAbsolutePath());
    }
    return fullPathList;
  }
}
