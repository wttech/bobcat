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
package com.cognifide.qa.bb.aem.touch.util;

import static com.cognifide.qa.bb.jcr.JcrExpectedConditions.nodeExist;
import static javax.jcr.Property.JCR_TITLE;
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
import com.google.inject.Inject;

/**
 * This class contains helper methods for jcr content operations.
 */
public class ContentHelper {

  public static final String JCR_CONTENT = "/jcr:content";

  private static final String CQ_TAG = "cq:Tag";

  private static final String TAGS_ROOT = "/etc/tags";

  private static final String SEPARATOR = "/";

  private final JcrHelper jcrHelper;

  private final Session session;

  @Inject
  private Conditions conditions;

  /**
   * Constructs ContentHelper and sets its {@link JcrHelper} jcrHelper and {@link Session} session fields.
   *
   * @param provider for {@link JcrHelper} jcrHelper and {@link Session} session fields.
   */
  @Inject
  public ContentHelper(JcrAuthorUtils provider) {
    this.jcrHelper = provider.getJcrHelper();
    this.session = provider.getSession();
  }

  /**
   * Creates tag node under given path, verifies its existance and return its full jcr path.
   *
   * @param tagPath tag path
   * @return full jcr path to created tag.
   * @throws RepositoryException when node creation fails.
   */
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

  /**
   * Checks if node exist under given path.
   *
   * @param path path to node.
   * @return true if node under given path exists.
   */
  public boolean isNodePresent(String path) {
    return conditions.isConditionMet(nodeExist(session, path));
  }

  /**
   * Changes title of node under given jcr path.
   *
   * @param path      jcr path to node.
   * @param newTitle title that will replace old one.
   * @throws RepositoryException if adding node property fails.
   */
  public void changeTitle(String path, String newTitle) throws RepositoryException {
    jcrHelper.addNodeProperty(path, JCR_TITLE, newTitle, 1);
  }

  /**
   * Changes name of node under given path, then returns its modified path.
   *
   * @param path    jcr path to the node.
   * @param newName node name that will replace old one.
   * @return jcr path with new node name.
   * @throws RepositoryException when operation on session fails (moving session to new path or saving changes).
   */
  public String changeName(String path, String newName) throws RepositoryException {
    String newPath = substringBeforeLast(path, SEPARATOR) + SEPARATOR + newName;
    session.move(path, newPath);
    session.save();
    return newPath;
  }

  /**
   * @param path jcr path of node.
   * @return title of page under given path.
   * @throws RepositoryException if retrieving node property fails.
   */
  public String getPageTitle(String path) throws RepositoryException {
    return jcrHelper.getNodeProperty(path + JCR_CONTENT, JCR_TITLE).getString();
  }

  /**
   * Deletes tag node under given tag path.
   *
   * @param tagPath path to tag.
   * @throws RepositoryException when node removing fails.
   */
  public void deleteTag(String tagPath) throws RepositoryException {
    deleteNode(TAGS_ROOT + tagPath);
  }

  private void deleteNode(String path) throws RepositoryException {
    jcrHelper.deleteNode(path, session);
  }
}
