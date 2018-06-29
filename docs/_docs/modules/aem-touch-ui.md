---
title: "Module: AEM Touch UI"
---

{% include under-construction.html %}

To get AEM Touch UI module to be ready to use follow these two steps:

1. Add dependency into your `pom.xml` file:

    ```xml
        <dependency>
            <groupId>com.cognifide.qa.bb</groupId>
             <artifactId>bb-aem-touch-ui</artifactId>
             <version>1.4.0</version>
        </dependency>
    ```
2. Add module installation to your `GuiceModule.java` file:
    ```java
        install(new AemTouchUiModule());
    ```
