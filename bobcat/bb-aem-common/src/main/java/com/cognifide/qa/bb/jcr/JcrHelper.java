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
package com.cognifide.qa.bb.jcr;

import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides methods for Jcr.
 */
public class JcrHelper {

  private static final Logger LOG = LoggerFactory.getLogger(JcrHelper.class);

  private final Session session;

  /**
   * Constructor. Initializes JcrHelper.
   *
   * @param session JCR session
   */
  public JcrHelper(Session session) {
    this.session = session;
  }

  /**
   * Gets node under specified path.
   *
   * @param nodePath Absolute path to node.
   * @return Node node
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public Node getNode(String nodePath) throws RepositoryException {
    session.refresh(true);
    return session.getNode(nodePath);
  }

  /**
   * Creates new node.
   *
   * @param parentNodePath Absolute path to parent node.
   * @param nodeName       The name of the node to create.
   * @param nodeType       Node type, like nt:unstructured.
   * @param properties     Map of properties, where key is property name,
   *                       value is {@link org.apache.commons.lang3.tuple.Pair}
   *                       of
   *                       <br>
   *                       &lt;property value, property type ({@link javax.jcr.PropertyType})&gt;.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void createNode(String parentNodePath, String nodeName, String nodeType,
      Map<String, Pair<String, Integer>> properties) throws RepositoryException {
    LOG.debug("Creating node '{}' of type '{}' under '{}'", nodeName, nodeType, parentNodePath);
    session.refresh(true);
    Node parentNode = session.getNode(parentNodePath);
    Node createdNode = parentNode.addNode(nodeName, nodeType);
    if (properties != null) {
      for (Map.Entry<String, Pair<String, Integer>> mapEntry : properties.entrySet()) {
        createdNode.setProperty(mapEntry.getKey(), mapEntry.getValue().getKey(), mapEntry.getValue()
            .getValue());
      }
    }
    session.save();
  }

  /**
   * Creates new node.
   *
   * @param parentNodePath Absolute path to parent node.
   * @param nodeName       The name of the node to create.
   * @param nodeType       Node type, like nt:unstructured.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void createNode(String parentNodePath, String nodeName, String nodeType)
      throws RepositoryException {
    createNode(parentNodePath, nodeName, nodeType, null);
  }

  /**
   * Copies nodes.
   *
   * @param nodePath        Absolute path to node.
   * @param destinationPath Absolute path to new node parent.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void copyNode(String nodePath, String destinationPath) throws RepositoryException {
    copyNode(getNode(nodePath), getNode(destinationPath));
    session.save();
  }

  /**
   * Copies nodes.
   *
   * @param node              Node to copy
   * @param destinationParent Destination parent node
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void copyNode(Node node, Node destinationParent) throws RepositoryException {
    LOG.debug("Copying node '{}' into '{}'", node.getPath(), destinationParent.getPath());
    Node newNode = destinationParent.addNode(node.getName(), node.getPrimaryNodeType().getName());
    PropertyIterator it = node.getProperties();
    while (it.hasNext()) {
      Property property = it.nextProperty();
      if (!property.getDefinition().isProtected()) {
        newNode
            .setProperty(property.getName(), property.getValue().getString(), property.getType());
      }
    }
    NodeIterator nodeIterator = node.getNodes();
    while (nodeIterator.hasNext()) {
      copyNode(nodeIterator.nextNode(), newNode);
    }
  }

  /**
   * Deletes node under specified path.
   *
   * @param nodePath Absolute path to node.
   * @param session  jcr session
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void deleteNode(String nodePath, Session session) throws RepositoryException {
    LOG.debug("Deleting node '{}'", nodePath);
    if (session.nodeExists(nodePath)) {
      session.removeItem(nodePath);
      session.save();
    }
  }

  /**
   * Adds property to node under specified path.
   *
   * @param nodePath      Absolute path to node.
   * @param propertyName  Property name.
   * @param propertyValue Property value.
   * @param propertyType  Property type {@link javax.jcr.PropertyType}.
   * @return Node property.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public Property addNodeProperty(String nodePath, String propertyName, String propertyValue,
      int propertyType) throws RepositoryException {
    LOG.debug("Adding property '{}'='{}' to node '{}'", propertyName, propertyValue, nodePath);
    session.refresh(true);
    Node node = session.getNode(nodePath);
    Property property = node.setProperty(propertyName, propertyValue, propertyType);
    session.save();
    return property;
  }

  /**
   * Removes node property.
   *
   * @param nodePath     Absolute path to node.
   * @param propertyName Property name.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public void removeNodeProperty(String nodePath, String propertyName) throws RepositoryException {
    LOG.debug("Removing property '{}' from node '{}'", propertyName, nodePath);
    session.refresh(true);
    session.getNode(nodePath).getProperty(propertyName).setValue((String) null);
    session.save();
  }

  /**
   * @param nodePath     Absolute path to node.
   * @param propertyName Property name.
   * @return Node property.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public Property getNodeProperty(String nodePath, String propertyName) throws RepositoryException {
    session.refresh(true);
    return session.getNode(nodePath).getProperty(propertyName);
  }

  /**
   * Checks if node has specified property
   *
   * @param nodePath     Absolute path to node.
   * @param propertyName Property name.
   * @return True if node has property.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public Boolean hasNodeProperty(String nodePath, String propertyName) throws RepositoryException {
    LOG.debug("Checking if node '{}' has property '{}'", nodePath, propertyName);
    session.refresh(true);
    return session.getNode(nodePath).hasProperty(propertyName);
  }

  /**
   * Checks if specified node property has specified value
   *
   * @param nodePath      Absolute path to node.
   * @param propertyName  Property name.
   * @param propertyValue Property value.
   * @return True if node property has specified value.
   * @throws RepositoryException if problem with jcr repository occurred
   */
  public Boolean hasNodePropertyValue(String nodePath, String propertyName, String propertyValue)
      throws RepositoryException {
    LOG.debug("Checking if node '{}' has property '{}' with value '{}'", nodePath, propertyName,
        propertyValue);
    return getNodeProperty(nodePath, propertyName).getValue().getString().equals(propertyValue);
  }
}
