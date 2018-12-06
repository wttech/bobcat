---
title: "Component configuration"
---

## Overview

Bobcat allows to configure AEM components in author mode using configuration files
To configure the component we need to know two things.

- Where the component is in content tree
- What is the component configuration

## Where is component
```java
controller.execute(AemActions.CONFIGURE_COMPONENT,
        new ConfigureComponentData("container", "Text", 0,
            new ResourceFileLocation("text.yaml")));
```

To configure component we use action with specific parameters:

- place in content tree
- component name
- which component if there is many of them
- configuration file

Last one is described in another section. Lets focus on first three. 

AEM Content Tree

![Login Page]({{site.baseurl}}/assets/img/componentconfigure.png)

Component create tree structure where containers can be described as branches and components as leaves.

### Place in content tree
The first parameter can be also described as selecting a branch. Each container is another level of branches. 
For example to select Text component with test "test test test" our parameter would be simple" `container` but for Form Options would be `container/container`

What happens if there are many containers on the same level:
```
- container 
  - container
  - container
    - Text
``` 
In that case we need to tell Bobcat which container should be used (we treat first container as '0'). 

To find Text component: `container/container[1]`
### Component name
This one is simple we look into content tree as choose component name
 
### Which component
We can have many components with the same name on the same branch. In our image we have two Text components so the first one will be '0', second '1'

## Configuration file

