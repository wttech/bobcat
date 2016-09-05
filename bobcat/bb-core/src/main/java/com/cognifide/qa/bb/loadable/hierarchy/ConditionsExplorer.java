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
package com.cognifide.qa.bb.loadable.hierarchy;

import com.cognifide.qa.bb.loadable.context.ComponentContext;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ConditionsExplorer {

  private static final Logger LOG = LoggerFactory.getLogger(ConditionsExplorer.class);

  private final ConditionHierarchyNode treeRootNode = new ConditionHierarchyNode(null);

  @Inject
  private PageObjectInjector injector;

  /**
   *
   * @param clazz
   * @param loadableContext
   * @return
   */
  public ConditionStack discoverLoadableContextHierarchy(Class clazz,
          ClassFieldContext loadableContext) {
    Stack<ComponentContext> stack = new Stack<>();
    if (loadableContext != null) {
      for (ComponentContext context : loadableContext.toLoadableContextList()) {
        stack.add(context);
      }
    }
    ConditionHierarchyNode treeNode = findNode(clazz, treeRootNode);
    if (treeNode == null) {
      LOG.debug("Didin't found class {} in the loadable component hierarchy tree", clazz.getName());
    }

    while (treeNode != null) {
      for (ComponentContext lodableContext : treeNode.getLoadableFieldContext().
              toLoadableContextList()) {
        stack.add(lodableContext);
      }
      treeNode = treeNode.getParent();
    }

    return new ConditionStack(stack);
  }

  public void registerLoadableContextHierarchyTree(Class clazz) {
    if (!treeAlreadyBuilt(clazz)) {
      LOG.debug("Building loadable component condition hierarchy tree from {}", clazz.getName());
      treeRootNode.setLoadableFieldContext(new ClassFieldContext(clazz, Collections.emptyList()));
      processLoadableContextForClass(clazz, treeRootNode);
    }
  }

  private void processLoadableContextForClass(Class clazz, ConditionHierarchyNode parent) {
    List<Field> decalredFields = Arrays.asList(clazz.getDeclaredFields());
    List<Field> applicableFields = decalredFields.stream()
            .filter(f -> f.isAnnotationPresent(Inject.class))
            .filter(f -> f.getType().isAnnotationPresent(PageObject.class))
            .collect(Collectors.toList());

    //TODO Check here if page object field is the only injected field of this type. If not throw an error

    for (Field field : applicableFields) {
      ConditionHierarchyNode node = addChild(parent, new ClassFieldContext(injector.inject(field.
              getType()), getLoadableConditionsFromField(field)));
      processLoadableContextForClass(field.getType(), node);
    }
  }

  private ConditionHierarchyNode addChild(ConditionHierarchyNode parent,
          ClassFieldContext loadableContext) {
    ConditionHierarchyNode node = new ConditionHierarchyNode(parent);
    node.setLoadableFieldContext(loadableContext);
    parent.getChildren().add(node);
    return node;
  }

  private boolean treeAlreadyBuilt(Class testClass) {
    return treeRootNode.getLoadableFieldContext() != null
            && treeRootNode.getLoadableFieldContext().getSubject().getClass().equals(testClass);
  }

  private ConditionHierarchyNode findNode(Class clazz, ConditionHierarchyNode parent) {
    Class elementClass = AopUtil.getBaseClassForAopObject(parent.getLoadableFieldContext().getSubject());
    if (elementClass.equals(clazz)) {
      return parent;
    } else {

      for (ConditionHierarchyNode node : parent.getChildren()) {
        ConditionHierarchyNode result = findNode(clazz, node);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  private List<ConditionContext> getLoadableConditionsFromField(Field field) {
    List<ConditionContext> result = new ArrayList<>();
    List<LoadableComponent> loadableAnnotations = Arrays.asList(field.
            getAnnotationsByType(LoadableComponent.class));
    if (!loadableAnnotations.isEmpty()) {
      for (LoadableComponent loadableComponent : loadableAnnotations) {
        result.add(new ConditionContext(loadableComponent, field.getName(), field.getDeclaringClass().
                getName()));
      }
    }
    return result;
  }
}
