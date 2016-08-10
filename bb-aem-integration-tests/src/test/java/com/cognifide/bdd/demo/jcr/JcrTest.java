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
package com.cognifide.bdd.demo.jcr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.jcr.JcrExpectedConditions;
import com.cognifide.qa.bb.jcr.JcrHelper;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.jcr.utils.JcrAuthorUtils;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class JcrTest {

  private static final String NODE_PATH = "/content/test";

  private static final String COPIED_NODE_PATH = "/content/sites/test";

  private Session session;

  private JcrHelper jcrHelper;

  @Inject
  private JcrAuthorUtils provider;

  @Inject
  private WebElementUtils webElementUtils;

  @Before
  public void before() throws RepositoryException {
    this.session = provider.getSession();
    this.jcrHelper = provider.getJcrHelper();
    jcrHelper.deleteNode(NODE_PATH, session);
    jcrHelper.deleteNode(COPIED_NODE_PATH, session);
  }

  @Test
  public void getNodeTest() throws RepositoryException {
    Node node = jcrHelper.getNode("/content/geometrixx");
    assertThat("Node is null", node, is(notNullValue()));
    assertThat("Invalid node name", node.getName(), is("geometrixx"));
  }

  @Test
  public void createNode() throws RepositoryException {
    jcrHelper.createNode("/content", "test", "nt:unstructured");
    webElementUtils
        .isConditionMet(JcrExpectedConditions.nodeExist(session, NODE_PATH), Timeouts.MINIMAL);
  }

  @Test
  public void deleteNodeTest() throws RepositoryException {
    jcrHelper.createNode("/content", "test", "nt:unstructured");
    assertTrue("Node does not exist", session.nodeExists(NODE_PATH));
    jcrHelper.deleteNode(NODE_PATH, session);
    assertFalse("Node still exists", session.nodeExists(NODE_PATH));
  }

  @Test
  public void copyNodeTest() throws RepositoryException {
    jcrHelper.createNode("/content", "test", "nt:unstructured", getPropertiesMap());
    assertTrue("Node does not exist", session.nodeExists(NODE_PATH));
    assertTrue("Wrong node property", jcrHelper.hasNodePropertyValue(NODE_PATH, "property1", "value1"));
    jcrHelper.createNode(NODE_PATH, "test1", "nt:unstructured", getPropertiesMap());
    String NODE_CHILD_PATH = "/content/test/test1";
    assertTrue("Node does not exist", session.nodeExists(NODE_CHILD_PATH));
    assertTrue("Wrong node property", jcrHelper.hasNodePropertyValue(NODE_CHILD_PATH, "property1", "value1"));
    jcrHelper.copyNode(NODE_PATH, "/content/sites");
    assertTrue("Node does not exist", session.nodeExists(COPIED_NODE_PATH));
    assertTrue("Wrong node property", jcrHelper.hasNodePropertyValue(COPIED_NODE_PATH, "property1", "value1"));
    String COPIED_NODE_CHILD_PATH = "/content/sites/test/test1";
    assertTrue("Node does not exist", session.nodeExists(COPIED_NODE_CHILD_PATH));
    assertTrue("Wrong node property", jcrHelper.hasNodePropertyValue(COPIED_NODE_CHILD_PATH, "property1", "value1"));
  }

  @Test
  public void nodePropertyTest() throws RepositoryException {
    jcrHelper.createNode("/content", "test", "nt:unstructured", getPropertiesMap());
    assertTrue("Wrong node property", jcrHelper.hasNodeProperty(NODE_PATH, "property1"));
    assertTrue("Wrong node property", jcrHelper.hasNodePropertyValue(NODE_PATH, "property1", "value1"));
    jcrHelper.addNodeProperty(NODE_PATH, "property2", "3.0", 4);
    assertTrue("Wrong node property", jcrHelper.hasNodeProperty(NODE_PATH, "property2"));
    jcrHelper.removeNodeProperty(NODE_PATH, "property1");
    assertFalse("Wrong node property", jcrHelper.hasNodeProperty(NODE_PATH, "property1"));
  }

  @Test
  public void jcrExpectedConditionsTest() throws RepositoryException {
    jcrHelper.createNode("/content", "test", "nt:unstructured", getPropertiesMap());
    webElementUtils
        .isConditionMet(JcrExpectedConditions.hasNodeProperty(session, NODE_PATH, "property1"),
            Timeouts.MINIMAL);
    webElementUtils.isConditionMet(
        JcrExpectedConditions.hasNodePropertyValue(session, NODE_PATH, "property1", "value1"),
        Timeouts.MINIMAL);
  }

  public Map<String, Pair<String, Integer>> getPropertiesMap() {
    Map<String, Pair<String, Integer>> property = new HashMap<>();
    Pair<String, Integer> entry = new MutablePair<>("value1", 1);
    property.put("property1", entry);
    return property;
  }
}
