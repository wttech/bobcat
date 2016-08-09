package com.cognifide.qa.bb.mapper.field;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.nestedselector.NestedSelectorScopedLocatorFactory;
import com.cognifide.qa.bb.utils.AnnotationsHelper;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This provider produces values for PageObject's fields that are annotated with FindPageObject
 * annotation. It tracks the context in which the objects are created so that their own child
 * objects can reference their parent's creation context.
 */
public class CssPageObjectProvider implements FieldProvider {

  @Inject
  private ContextStack contextStack;

  @Inject
  private Injector injector;

  @Inject
  private WebDriver webDriver;

  @Inject
  private FrameMap frameMap;

  @Override
  public boolean accepts(Field field) {
    return field.getType().isAnnotationPresent(PageObject.class)
        && AnnotationsHelper.isFindPageObjectAnnotationPresent(field) && isNotList(field);

  }

  /**
   * This method produces value for the field. It constructs the context for the creation out of
   * paren't context and the field's own frame info.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    By selector = By.cssSelector(field.getType().getAnnotation(PageObject.class).css());
    ElementLocatorFactory elementLocatorFactory =
        new NestedSelectorScopedLocatorFactory(webDriver, selector,
            context.getElementLocatorFactory(),AnnotationsHelper.isGlobal(field));
    final FramePath framePath = frameMap.get(pageObject);
    contextStack.push(new PageObjectContext(elementLocatorFactory, framePath));
    Object scopedPageObject = null;
    try {
      scopedPageObject = injector.getInstance(field.getType());
    } finally {
      contextStack.pop();
    }
    return Optional.ofNullable(scopedPageObject);
  }

  private boolean isNotList(Field field) {
    return !field.getType().equals(List.class);
  }
}
