---
title: "Actions"
---

Available in `bb-core` since version `2.0.0`
{: .notice--info}

## Overview
Since 2.0 we provide an Actions mechanism, that can be utilized to build... well, actions in a unified way :). They can be executed the same way, using a single `ActionController`.

This way, we've got rid of multiple helpers, utilities or very specialized page objects that you had to know about in the first place :).

What can be an action? Basically any set of instructions you like to wrap together to provide a more concise DSL, e.g. creating a new page. 

## Types of actions

There are two types of actions, defined by following interfaces:
- `Action` 
- `ActionWithData`

### Simple actions
`Action` describes the simplest type of actions. It is defined in an `execute()` method:

```java
class MyAction implements Action {
  @Override
  public void execute() {
    //my action
  }
}
```

Next, you need to bind it in your Guice module using the appropriate `MapBinder`.

Actions should be bound to their `String` identifier, which will allow to easily reference them in the future.

```java
  // for Action
    MapBinder<String, Action> actions = MapBinder.newMapBinder(binder(), String.class, Action.class);
    actions.addBinding("My action").to(MyAction.class);
```

### Actions with data
The `ActionWithData` interface serves more advanced types of actions that need to consume some additional data. It is defined in an `execute(T data)` method:

```java
class MyActionWitData implements ActionWitData<MyData> {
  @Override
  public void execute(MyData data) {
    //my action
  }
}
```

Same with `Action`, you need to provide appropriate bindings in your module:

```java
  // for ActionWithData
  MapBinder<String, ActionWithData> actionsWithData = MapBinder.newMapBinder(binder(), String.class, ActionWithData.class);
  actionsWithData.addBinding("My action with data").to(MyActionWithData.class);
```

## Injecting PageObjects
Thanks to the fact that the actions are bound with the `MapBinder`, you can utilize all of Bobcat's functionality inside of them. One of them is e.g. using page objects, with their appropriate scope.

An example, from Bobcat's internals:

```java
package com.cognifide.qa.bb.aem.core.siteadmin.actions;

@PageObject
public class CreatePageAction implements ActionWithData<CreatePageActionData> {

  @FindPageObject
  private SiteToolbar toolbar;

  @Override
  public void execute(CreatePageActionData data) {
    toolbar.createPage(data.getTemplate(), data.getTitle(), data.getPageName());
  }
}
```

Please note, that as with regular page objects, you need to annotate the action with the `@PageObject`. Thanks to that, Bobcat will pick up and handle its specific decorators, like the `@FindPageObject` above.
{: .notice--info}

## Decorating actions with Allure annotations
You can utilize the capabilities of the Allure reporting engine and make the reports even more readable when implementing your own actions.

It just takes a single annotation, `@Step`:
```java
package com.cognifide.qa.bb.aem.core.siteadmin.actions;

@PageObject
public class CreatePageAction implements ActionWithData<CreatePageActionData> {

  @FindPageObject
  private SiteToolbar toolbar;

  @Override
  @Step("Create page {data.title} with name {data.pageName} using {data.template} template")
  public void execute(CreatePageActionData data) {
    toolbar.createPage(data.getTemplate(), data.getTitle(), data.getPageName());
  }
}
```
and after executing your action, you will get the following in the report:

![Action in Allure]({{site.baseurl}}/assets/img/action-step-allure.png)

## Executing actions

After implementing one of the two above interfaces, we need a way to execute it! `ActionController` will help us with that :).

We simply inject it in our test class and execute the action, referencing it with the corresponding identifier:

```java
@Inject
private ActionController actions;

actions.execute("MyAction");
actions.execute("MyActionWithData", new MyData("some data"));
```

### Custom controllers
If for some reason you're not happy with the default controller shipped with Bobcat, you can simply provide your own implementation and then override the `ActionsModule` module bindings with your own. 
 