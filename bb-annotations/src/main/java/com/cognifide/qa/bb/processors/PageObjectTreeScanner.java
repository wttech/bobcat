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
package com.cognifide.qa.bb.processors;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.google.inject.Inject;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

import org.openqa.selenium.WebElement;

class PageObjectTreeScanner extends TreePathScanner<Object, CompilationUnitTree> {

  private static final String CURRENT_SCOPE_FIELD_NAME = "currentScope";

  private final Trees trees;

  private final TreeMaker treeMaker;

  private final ProcessingEnvironment processingEnv;

  public PageObjectTreeScanner(Trees trees, TreeMaker treeMaker, ProcessingEnvironment processingEnv) {
    this.trees = trees;
    this.treeMaker = treeMaker;
    this.processingEnv = processingEnv;
  }

  @Override
  public Trees visitClass(ClassTree ct, CompilationUnitTree unitTree) {
    if (unitTree instanceof JCCompilationUnit) {
      final JCCompilationUnit compilationUnit = (JCCompilationUnit) unitTree;
      // Only process on files which have been compiled from source
      if (compilationUnit.sourcefile.getKind() == JavaFileObject.Kind.SOURCE) {

        compilationUnit.accept(new TreeTranslator() {

          @Override
          public void visitClassDef(JCTree.JCClassDecl classDef) {
            super.visitClassDef(classDef);
            Name name = (Name) processingEnv.getElementUtils().getName(CURRENT_SCOPE_FIELD_NAME);

            if (!containsField(classDef, name)) {
              Type webElementType = getType(WebElement.class);
              Type currentScopeType = getType(CurrentScope.class);
              Type googleInjectType = getType(Inject.class);

              Compound currentScopeCompund = new Compound(currentScopeType, List.nil());
              Compound injectCompound = new Compound(googleInjectType, List.nil());

              List<JCAnnotation> annotations = treeMaker.
                      Annotations(List.of(currentScopeCompund, injectCompound));

              JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PRIVATE, annotations);

              addFieldToClass(jcModifiers, name, webElementType, classDef);
            }
          }

          private void addFieldToClass(JCModifiers jcModifiers, Name name, Type type,
                  JCTree.JCClassDecl classDef) {

            JCVariableDecl currentScopeField = treeMaker.
                    VarDef(jcModifiers, name, treeMaker.Type(type), null);

            classDef.defs = classDef.defs.prepend(currentScopeField);
          }
        });
      }
    }
    return trees;
  }

  private Type getType(Class clazz) {
    return (Type) processingEnv.getElementUtils().
            getTypeElement(clazz.getCanonicalName()).asType();
  }

  private boolean containsField(JCTree.JCClassDecl classDef, Name name) {
    return classDef.defs.stream()
            .filter(t -> t instanceof JCVariableDecl)
            .map(elem -> (JCVariableDecl) elem)
            .anyMatch(var -> var.getName().equals(name));
  }

}
