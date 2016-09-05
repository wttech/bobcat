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

import java.util.ArrayList;
import java.util.List;

class ConditionHierarchyNode {

  private final List<ConditionHierarchyNode> children = new ArrayList<>();

  private final ConditionHierarchyNode parent;

  private ClassFieldContext loadableFieldContext;

  public ConditionHierarchyNode(ConditionHierarchyNode parent) {
    this.parent = parent;
  }

  public ClassFieldContext getLoadableFieldContext() {
    return loadableFieldContext;
  }

  public void setLoadableFieldContext(ClassFieldContext context) {
    this.loadableFieldContext = context;
  }

  public List<ConditionHierarchyNode> getChildren() {
    return children;
  }

  public ConditionHierarchyNode getParent() {
    return parent;
  }
}
