/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.loadablecomponent;

import com.cognifide.qa.bb.mapper.tree.LoadableContext;
import com.cognifide.qa.bb.mapper.tree.Node;
import com.cognifide.qa.bb.qualifier.LoadableComponent;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Singleton
public class LoadableQualifiersExplorer {

  private final Node treeRootNode = new Node(null);

  @Inject
  private PageObjectInjector injector;

  /**
   *
   * @param clazz
   * @param loadableContext
   * @return
   */
  public LoadableQualifiersStack discoverLoadableContextAbove(Class clazz, LoadableContext loadableContext) {
    Stack<LoadableContext> stack = new Stack<>();
    if (loadableContext != null) {
      stack.add(loadableContext);
    }
    Node treeNode = findNode(clazz, treeRootNode);
    while (treeNode != null) {
      stack.add(treeNode.getLoadableContext());
      treeNode = treeNode.getParent();
    }

    return new LoadableQualifiersStack(stack);
  }

  public void registerLoadableContextTree(Class clazz) {
    if (!treeAlreadyBuilt(clazz)) {
      treeRootNode.setLoadableContext(new LoadableContext(clazz, Collections.emptyList()));
      processLoadableContextForClass(clazz, treeRootNode);
    }
  }

  private void processLoadableContextForClass(Class clazz, Node parent) {
    List<Field> decalredFields = Arrays.asList(clazz.getDeclaredFields());
    List<Field> applicableFields = decalredFields.stream()
            .filter(f -> f.isAnnotationPresent(Inject.class))
            .filter(f -> f.getType().isAnnotationPresent(PageObject.class))
            .collect(Collectors.toList());

    //TODO Check here if page object field is the only injected field of this type. If not throw an error

    for (Field field : applicableFields) {
      Node node = addChild(parent, new LoadableContext(injector.inject(field.getType()), getLoadablesFromField(field)));
      processLoadableContextForClass(field.getType(), node);
    }
  }

  private Node addChild(Node parent, LoadableContext loadableContext) {
    Node node = new Node(parent);
    node.setLoadableContext(loadableContext);
    parent.getChildren().add(node);
    return node;
  }

  private boolean treeAlreadyBuilt(Class testClass) {
    return treeRootNode.getLoadableContext() != null
            && treeRootNode.getLoadableContext().getElement().equals(testClass);
  }

  private Node findNode(Class clazz, Node parent) {
    Class elementClass = parent.getLoadableContext().getElement().getClass();
    if(elementClass.getName().contains("EnhancerByGuice")) {
      elementClass = elementClass.getSuperclass();
    }
    if (elementClass.equals(clazz)) {
      return parent;
    } else {

      for (Node node : parent.getChildren()) {
        Node result = findNode(clazz, node);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  private List<Loadable> getLoadablesFromField(Field field) {
    List<Loadable> result = new ArrayList<>();
    List<LoadableComponent> loadableAnnotations = Arrays.asList(field.
            getAnnotationsByType(LoadableComponent.class));
    if (!loadableAnnotations.isEmpty()) {
      for (LoadableComponent loadableComponent : loadableAnnotations) {
        result.add(new Loadable(loadableComponent));
      }
    }
    return result;
  }
}
