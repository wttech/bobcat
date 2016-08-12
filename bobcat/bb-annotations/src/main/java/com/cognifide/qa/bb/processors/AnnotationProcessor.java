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

import com.cognifide.qa.bb.qualifier.PageObject;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.annotation.processing.Messager;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * This annotation processor is responsible for adding currentScope field to the {@link PageObject}-annotated classes.
 * See the {@link PageObject} javadoc for more information.
 */
@SupportedAnnotationTypes(
        {"com.cognifide.qa.bb.qualifier.PageObject"}
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {

  private Trees trees;

  private TreeMaker treeMaker;

  private Messager messager;

  @Override
  public void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    trees = Trees.instance(processingEnv);
    Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
    treeMaker = TreeMaker.instance(context);
    messager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    PageObjectTreeScanner scanner = new PageObjectTreeScanner(trees, treeMaker, processingEnv);
    Set<Element> applicableElements = roundEnv.getElementsAnnotatedWith(PageObject.class).stream()
            .filter(element -> element.getKind() == ElementKind.CLASS)
            .filter(element -> element.getAnnotation(PageObject.class).generateCurrentScope())
            .collect(Collectors.toSet());

    applicableElements.stream()
            .forEach(element -> {
              TreePath path = trees.getPath(element);
              try {
                scanner.scan(path, path.getCompilationUnit());
              } catch (Exception ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Error occured while annotation processing", element);
              }
            });
    return true;
  }
}
