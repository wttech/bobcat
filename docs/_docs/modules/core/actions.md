---
title: "Actions"
---

Available in `bb-core` since version `2.0.0`
{: .notice--info}

## Overview
Since 2.0 we provide an Actions mechanism, that can be utilized to build... well, actions in a unified way :). They can be executed the same way, using an ActionController.

Decorating these actions with Allure annotations also provides reports rich with additional insight.

## Types of actions

There are two types of actions, defined by following interfaces:
- `Action` - the simplest type, a straightforward action that is defined in `execute()`
- `ActionWithData` - a more advanced type, an action is executed with additional data and is defined `execute(T data)`

Based on your needs, you need to create an implementation of one of the above. Then, you need to bind it in your Guice module using the appropriate `MapBinder`.

Actions should be bound to their `String` identifier, which will allow to easily reference them in the future.

```java
  // for Action
    MapBinder<String, Action> actions = MapBinder.newMapBinder(binder(), String.class, Action.class);
    actions.addBinding("My action").to(MyAction.class);
  
  // for ActionWithData
  MapBinder<String, ActionWithData> actionsWithData = MapBinder.newMapBinder(binder(), String.class, ActionWithData.class);
  actionsWithData.addBinding("My action with data").to(MyActionWithData.class);
```

where:

```java
class MyAction implements Action {
  @Override
  public void execute() {
    //my action
  }
}

class MyActionWitData implements ActionWitData<MyData> {
  @Override
  public void execute(MyData data) {
    //my action
  }
}
```

## Using actions with data


## Injecting PageObjects


## Decorating actions with Allure annotations


## Executing actions


## Extending the mechanism
Requires basic understanding of Guice.
{: .notice--warning}

If you would like to provide another way of loading the cookies, you can create your own provider, by implementing the `Provider<List<CookieData>` interface.

Then, you need to provide proper bindings, replacing the ones in `CookiesModule` that comes out-of-the-box with Bobcat. 