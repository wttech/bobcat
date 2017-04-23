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
package com.cognifide.qa.bb.aem.content;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.google.gson.JsonObject;
import com.google.inject.Inject;

/**
 * This is a utility class that lets the user install and activate AEM packages.
 */
public class ContentInstaller {

  private static final String CONTENT_PATH = System.getProperty("content.path", "src/main/content");

  @Inject
  private CrxRequestSender sender;

  @Inject
  private RequestBuilder builder;

  /**
   * This method requests AEM to install a package. Package is identified by the package's name,
   * provided as the method's parameter. installAemPackage sends the request as a POST request
   * and checks the response. If the response is NOK, then installAemPackage throws an IOException.
   * <br>
   * The package is installed in the AEM instance indicated by the author.ip property.
   *
   * @param packageName Name of the package to be installed.
   * @throws IOException Thrown when AEM instance returns NOK response.
   */
  public void installAemPackage(String packageName) throws IOException {
    String packagePath = String.format("/etc/packages/%s.zip", packageName);
    HttpPost request = builder.createInstallRequest(packagePath);
    sender.sendCrxRequest(request);
  }

  /**
   * This method uploads, installs and replicates the package indicated by the name
   * provided as the method's parameter. For each of these actions activateAemPackage constructs
   * and sends a POST request to AEM instance. If any of the POST requests lead to NOK response,
   * activateAemPackage will throw an exception.
   * <br>
   * Method will look for content in the location indicated by the content.path property.
   * The content path defaults to "src/main/content".
   *
   * @param packageName Name of the package to be activated.
   * @throws IOException Thrown when AEM instance returns NOK response.
   */
  public void activateAemPackage(String packageName) throws IOException {
    HttpPost upload = builder.createUploadRequest();
    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    File file = new File(CONTENT_PATH, packageName);
    if(!file.exists()) {
      throw new IllegalArgumentException("The provided package doesn't exist: " + file.getPath());
    }
    entityBuilder.addBinaryBody("package", file,
        ContentType.DEFAULT_BINARY, packageName);
    entityBuilder.addTextBody("force", "true");
    upload.setEntity(entityBuilder.build());
    JsonObject result = sender.sendCrxRequest(upload);
    String path = result.get("path").getAsString();

    HttpPost install = builder.createInstallRequest(path);
    sender.sendCrxRequest(install);

    HttpPost replicate = builder.createReplicateRequest(path);

    sender.sendCrxRequest(replicate);
  }
}
