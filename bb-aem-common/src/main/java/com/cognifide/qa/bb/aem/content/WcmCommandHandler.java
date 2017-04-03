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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class WcmCommandHandler {

  public static final String PATH_PROPERTY = "path";

  public static final String CMD_PROPERTY = "cmd";

  public static final String CHARSET_PROPERTY = "_charset_";

  public static final String PARENT_PATH_PROPERTY = "parentPath";

  public static final String TITLE_PROPERTY = "title";

  public static final String TEMPLATE_PROPERTY = "template";

  private static final String REPLICATION_STARTED_MESSAGE = "Replication started for ";

  private static final String ACTIVATE = "Activate";

  private static final String DEACTIVATE = "Deactivate";

  private static final String UTF_8 = "utf-8";

  private static final String CREATE_PAGE_COMMAND = "createPage";

  private static final String DELETE_PAGE_COMMAND = "deletePage";

  private static final String PAGE_CREATED_SUCCESS_MESSAGE = "Page created";

  private static final String PAGE_DELETED_SUCCESS_MESSAGE_FORMAT = "Deleted %s";

  private static final String REPLICATE_URL = "%s/bin/replicate.json";

  private static final String WCM_COMMAND_URL = "%s/bin/wcmcommand";

  @Inject
  private CrxRequestSender sender;

  @Inject
  @Named(AemConfigKeys.AUTHOR_IP)
  private String authorIp;

  /**
   * Activates provided asset
   *
   * @param assetPath path to asset
   * @throws IOException if response doesn't contain desired message
   */
  public void activatePage(String assetPath) throws IOException {
    triggerAction(Maps.newHashMap(new ImmutableMap.Builder<String, String>()
        .put(PATH_PROPERTY, assetPath)
        .put(CMD_PROPERTY, ACTIVATE)
        .put(CHARSET_PROPERTY, UTF_8).build()), REPLICATE_URL,
        REPLICATION_STARTED_MESSAGE + assetPath);
  }

  /**
   * Deactivates provided asset
   *
   * @param assetPath path to asset
   * @throws IOException if response doesn't contain desired message
   */
  public void deactivatePage(String assetPath) throws IOException {
    triggerAction(Maps.newHashMap(new ImmutableMap.Builder<String, String>()
        .put(PATH_PROPERTY, assetPath)
        .put(CMD_PROPERTY, DEACTIVATE)
        .put(CHARSET_PROPERTY, UTF_8).build()), REPLICATE_URL,
        REPLICATION_STARTED_MESSAGE + assetPath);
  }

  /**
   * Create page in provided location
   *
   * @param parentPath path to parent page
   * @param title page title
   * @param template page template
   * @throws IOException if response doesn't contain desired message
   */
  public void createPage(String parentPath, String title, String template) throws IOException {
    triggerAction(Maps.newHashMap(new ImmutableMap.Builder<String, String>()
        .put(CMD_PROPERTY, CREATE_PAGE_COMMAND)
        .put(CHARSET_PROPERTY, UTF_8)
        .put(PARENT_PATH_PROPERTY, parentPath)
        .put(TITLE_PROPERTY, title)
        .put(TEMPLATE_PROPERTY, template).build()), WCM_COMMAND_URL, PAGE_CREATED_SUCCESS_MESSAGE);
  }

  /**
   * Delete page from provided location
   *
   * @param path to page
   * @throws IOException if response doesn't contain desired message
   */
  public void deletePage(String path) throws IOException {
    triggerAction(Maps.newHashMap(new ImmutableMap.Builder<String, String>()
        .put(CMD_PROPERTY, DELETE_PAGE_COMMAND)
        .put(CHARSET_PROPERTY, UTF_8)
        .put(PATH_PROPERTY, path.toLowerCase()).build()), WCM_COMMAND_URL,
        String.format(PAGE_DELETED_SUCCESS_MESSAGE_FORMAT, path.toLowerCase()));
  }

  /**
   * Allows to trigger any Wcm action using provided Map as POST properties
   *
   * @param postProperties properties map to send
   * @param successMessage expected success message
   * @param commandUrl command
   * @throws IOException if response doesn't contain desired message
   */
  public void triggerAction(Map<String, String> postProperties, String commandUrl,
      String successMessage)
      throws IOException {
    HttpPost request = new HttpPost(String.format(commandUrl, authorIp));
    List<BasicNameValuePair> params = new ArrayList<>();
    postProperties.entrySet().stream().
            forEach(property ->
              params.add(new BasicNameValuePair(property.getKey(), property.getValue()))
            );
    request.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
    sender.sendCrxRequest(request, successMessage);
  }
}
