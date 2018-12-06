---
title: "PageObject and PageObjectInterface"
---

## Overview

Selenium references: [PageObjects](https://github.com/SeleniumHQ/selenium/wiki/PageObjects)

`PageObject` is a central concept of the Bobcat. It's a term derived from Selenium framework and it means a class that encapsulates page HTML and exposes page features.

`PageObjectInterface` extends the idea of `PageObject`. It allows to define interface with API that can be bind with specific `PageObject` implementation. We can inject interface marked by `PageObjectInterface` annotation and Bobcat will found bound implementation and inject it instead.

What is important that class marked with `PageObject` doesn't require corresponding interface. You can still inject class marked by `PageObject`  It is additional feature to create one API for changing markup.

### Example:

Interface with `PageObjectInterface`
```java
@PageObjectInterface
public interface LoginComponent{
     
    public void login(String username, String password);
    
}
```

Implementation of this interface:
```java
@PageObject(css = ".login-box")
public class LoginComponentImpl extends LoginComponent{
     
    @FindBy(id = "username")
    private WebElement usernameField;
      
    @FindBy(id = "password")
    private WebElement passwordField;
        
    @FindBy(css = "button[type=submit]")
    private WebElement submitButton;
     
    public void login(String username, String password) {
         usernameField.sendKeys(username);
         passwordField.sendKeys(password);
         submitButton.click();
    }
    
}
```

Somewhere in guice module:

```java
public class LoginModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(LoginComponent.class).to(LoginComponentImpl.class);
  }
}
```

Page using component:
```java
@PageObject
public class LoginPage {
    
    @FindPageObject
    private LoginComponent loginComponent;
 
    public void login(String username, String password) {
        loginComponent.login(username, password);
    }
}
```

## `@PageObject` locators and `@FindPageObject`

`@PageObject` annotation can have locators that allows to find object in html markup (css or xpath). If our page object has locator set and we inject it using @FindPageObject then Bobcat will find it for us.

```java
@PageObject(xpath = "//div[contains(@class, 'x-grid3-body')]/div")
public class SomeElement {
//... 
}

@PageObject
public class TestObject {

  @FindPageObject
  private SomeElement someElement;

  @FindPageObject
  private List<SomeElement> items;

}
``` 

As we can see in example in [Overview](#overview) this mechanism will work if we are injecting interface bound to class with `@PageObject` and locator

## `@FindBy` annotations

Writing `webDriver.findElement(...)` is not very efficient. Luckily, Selenium provides the PageFactory util class that allows us to use `@FindBy annotations`. You don't have to call the PageFactory manually. If you annotate the class with `@PageObject`, Bobcat will do it for you:

```java
@PageObject // we need this so Bobcat takes care of @FindBy fields
public class LoginPage {
 
    @FindBy(id = "username")
    private WebElement usernameField;
 
    @FindBy(id = "password")
    private WebElement passwordField;
 
    @FindBy(css = "button[type=submit]")
    private WebElement submitButton;
 
    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitButton.click();
    }
}
```
In this case we use `FindBy` annotation in the `LoginPage` to inject three `WebElements`.

## Nested elements

Sometimes it may happen that HTML structure is too complicated to be represented by one class and it makes sense to split it into multiple classes. Consider a following page: 
![Login Page]({{site.baseurl}}/assets/img/pageobject.png)

Generally it's a good practice and one of basic Object Oriented principals to have classes with limited responsibility. But `FindBy`, `FindPageObject` annotation and `webdriver.findBy(...)` method uses global browser scope when seeking for elements. That is why, you may want to narrow scope for locating elements in `PageObject` class.
As we can see in example in [Overview](#overview) we have page with login component. We splitted implementation to `LoginPage` which represents outer html and `LoginComponentImpl` for login component nested in html element with class `login`. 

```java
@PageObject(css = ".login-box")
public class LoginComponentImpl extends LoginComponent{
     
    @FindBy(id = "username")
    private WebElement usernameField;
      
    @FindBy(id = "password")
    private WebElement passwordField;
        
    @FindBy(css = "button[type=submit]")
    private WebElement submitButton;
     
    public void login(String username, String password) {
         usernameField.sendKeys(username);
         passwordField.sendKeys(password);
         submitButton.click();
    }
    
}
```

Why is it so cool? The coolest part is under the hood: usernameField, passwordField and submitButton WebElements are located in scope limited to `div#login-box` element!

## `@CurrentScope`

What about a WebElement? Is it possible to have `div#login-box` as a WebElement that I can access to? Something like this object but for WebElements?
{: .notice--info}
Let's go back to our login page once more, but this time bare in mind that with Bobcat we can limit the scope for locating elements:
![Login Page]({{site.baseurl}}/assets/img/currentscope.png)
With `@CurrentScope` annotation we have an access to WebElement which represents the same limited scope that was used for locating `@FindBy` elements. In simple words: `WebElement scope = webdriver.findElement(By.cssSelector("div#login-box"))` will represent the same element as WebElement annotated with `@CurrentScope`:

```java
@PageObject(css = ".login-box")
public class LoginBox {
 
    @Inject
    @CurrentScope
    private WebElement currentScope; // it will be the div#login-box
 
 }
```

## @Global annotation
@Global annotation is the exact opposite to @CurrentScope. You may want to use it when there is a need to ignore scope limitation. But be careful and use it wisely. Possible use case: floating dialog.
```java
@PageObject(css = ".login-box")
public class LoginBox {
 
    @Global
    @FindBy(css = ".floating.dialog")
    private WebElement floatingDialog;
 
 }
```

Bobcat will search `floatingDialog` in whole html not only in `div#login-box`