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

import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.loadable.context.LoadableComponentContext;
import com.cognifide.qa.bb.loadable.hierarchy.util.LoadableComponentsUtil;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Field;
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
   * @param loadableFieldContext
   * @return
   */
  public ConditionStack discoverLoadableContextHierarchy(Class clazz,
          ClassFieldContext loadableFieldContext) {
    Stack<LoadableComponentContext> stack = new Stack<>();
    if (loadableFieldContext != null) {
      loadableFieldContext.toLoadableContextList().stream().
              forEach((context) -> {
                stack.add(context);
      });
    }
    ConditionHierarchyNode treeNode = findNode(clazz, treeRootNode);
    if (treeNode == null) {
      LOG.debug("Didin't found class {} in the loadable component hierarchy tree", clazz.getName());
    }

    while (treeNode != null) {
      for (LoadableComponentContext lodableContext : treeNode.getLoadableFieldContext().
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
    checkForDuplicatedLoadableFields(applicableFields);

    applicableFields.stream().
            forEach((field) -> {
              ConditionHierarchyNode node = addChild(parent, new ClassFieldContext(injector.inject(field.
                      getType()), LoadableComponentsUtil.getConditionsFormField(field)));
              processLoadableContextForClass(field.getType(), node);
    });
  }

  private void checkForDuplicatedLoadableFields(List<Field> applicableFields) {
    if (!applicableFields.isEmpty()) {
      int fieldCount = applicableFields.size();
      int typeCount = (int) applicableFields.stream().map(f -> f.getType()).distinct().count();
      if (typeCount < fieldCount) {
        throw new BobcatRuntimeException("Spotted two page object fields of the same type. This is illegal. "
                + "Class: " + applicableFields.get(0).getDeclaringClass().getName());
      }
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
}
