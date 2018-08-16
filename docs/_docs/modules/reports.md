---
title: "Module: Reports"
---

{% include under-construction.html %}

To get Reports module to be ready to use follow these two steps:

1. Add dependency into your `pom.xml` file:

    ```xml
    <dependency>
        <groupId>com.cognifide.qa.bb</groupId>
        <artifactId>bb-reports</artifactId>
        <version>1.4.0</version>
    </dependency>
    ```
2. Add module installation to your `GuiceModule.java` file:
    ```java
        install(new ReporterModule());
    ```
