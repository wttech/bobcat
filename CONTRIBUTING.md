Contributing to Bobcat
======================

Styleguides
-----------

### JAVA Styleguide

Use [Google Style][google-java-style] with following modifications:

1. line width: 100
2. preserve newlines (short lines may be more readable)
3. `@formatter:off` and `@formatter:on` disables and enables formatter
4. use eclipse imports order ([instructions for IntelliJ][eclipse-imports-order-in-intellij])

![imports order settings for IntelliJ][eclipse-imports-order-in-intellij-img]

#### Eclipse Formatter XML file

We provide [XML file for JAVA format][formatter-xml] that may be imported into Eclipse and IntelliJ.

[google-java-style]: https://google.github.io/styleguide/javaguide.html
[eclipse-imports-order-in-intellij]: http://stackoverflow.com/questions/14716283/is-it-possible-for-intellij-to-organize-imports-the-same-way-as-in-eclipse
[eclipse-imports-order-in-intellij-img]: contributing/intellij-imports-order.png
[formatter-xml]: contributing/eclipse-java-bobcat-style.xml
