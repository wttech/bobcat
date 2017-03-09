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

import org.apache.http.client.methods.HttpPost;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This is a utility class that lets the user create http request to crx.
 */
public class RequestBuilder {

  private static final String REQUEST_STRING = "%s/crx/packmgr/service/.json?cmd=%s";

  private static final String REQUEST_STRING_WITH_PATH = "%s/crx/packmgr/service/.json%s?cmd=%s";

  @Inject
  @Named(AemConfigKeys.AUTHOR_IP)
  private String authorIp;

  /**
   * Creates HttpPost request for upload
   *
   * @return HttpPost instance
   */
  public HttpPost createUploadRequest() {
    return new HttpPost(String.format(REQUEST_STRING, authorIp, Commands.UPLOAD.getCommand()));
  }

  /**
   * Creates HttpPost request for install
   * @param path requested path
   * @return HttpPost instance
   */
  public HttpPost createInstallRequest(String path) {
    return new HttpPost(
        String.format(REQUEST_STRING_WITH_PATH, authorIp, path, Commands.INSTALL.getCommand()));
  }

  /**
   * Creates HttpPost request for replication
   * @param path requested path
   * @return HttpPost instance
   */
  public HttpPost createReplicateRequest(String path) {
    return new HttpPost(
        String.format(REQUEST_STRING_WITH_PATH, authorIp, path, Commands.REPLICATE.getCommand()));
  }
}
