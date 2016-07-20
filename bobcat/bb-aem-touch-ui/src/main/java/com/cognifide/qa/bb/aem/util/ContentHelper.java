/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.util;

import static com.cognifide.qa.bb.jcr.JcrExpectedConditions.nodeExist;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.cognifide.qa.bb.jcr.JcrHelper;
import com.cognifide.qa.bb.provider.jcr.utils.JcrAuthorUtils;
import com.cognifide.qa.bb.aem.util.context.Context;
import com.google.inject.Inject;

public class ContentHelper {

  private static final String CQ_TAG = "cq:Tag";

  private static final String TAGS_ROOT = "/etc/tags";

  private static final String SEPARATOR = "/";

  public static final String JCR_TITLE = "jcr:title";

  public static final String JCR_CONTENT = "/jcr:content";

  private JcrHelper jcrHelper;

  private Session session;

  @Inject
  private Conditions conditions;

  @Inject
  private Context context;

  @Inject
  public ContentHelper(JcrAuthorUtils provider) {
    this.jcrHelper = provider.getJcrHelper();
    this.session = provider.getSession();
  }

  public String createTag(String tagPath) throws RepositoryException {
    String parentPath = TAGS_ROOT + substringBeforeLast(tagPath, SEPARATOR);
    String tagName = substringAfterLast(tagPath, SEPARATOR);
    String fullTagPath = parentPath + SEPARATOR + tagName;

    Map<String, Pair<String, Integer>> properties = new HashMap<>();
    properties.put(JCR_TITLE, new MutablePair<>(tagName, 1));

    jcrHelper.createNode(parentPath, tagName, CQ_TAG, properties);

    conditions.verify(nodeExist(session, fullTagPath));
    return fullTagPath;
  }

  public boolean isNodePresent(String path) {
    return conditions.isConditionMet(nodeExist(session, path));
  }

  public void changeTitle(String path, String newTitle) throws RepositoryException {
    jcrHelper.addNodeProperty(path, JCR_TITLE, newTitle, 1);
  }

  public String changeName(String path, String newName) throws RepositoryException {
    String newPath = substringBeforeLast(path, SEPARATOR) + SEPARATOR + newName;
    session.move(path, newPath);
    session.save();
    return newPath;
  }

  public String getPageTitle(String path) throws RepositoryException {
    return jcrHelper.getNodeProperty(path + JCR_CONTENT, JCR_TITLE).getString();
  }

  public void deleteNode(String path) throws RepositoryException {
    jcrHelper.deleteNode(path, session);
  }

  public void deleteTag(String tagPath) throws RepositoryException {
    deleteNode(TAGS_ROOT + tagPath);
  }
}
