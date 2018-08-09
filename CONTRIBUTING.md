# Contributing to Bobcat

Are you interested in contributing to Bobcat? Great!

## Reporting bugs

If you encounter a bug when using Bobcat, first, please check the existing [issues](https://github.com/Cognifide/bobcat/issues) - perhaps someone already had the same problem. If there is one, don't hesitate to add a comment with additional information!

If not, report the issue according to the template and set the label to `bug`.

## Proposing new features

Perhaps you have a cool idea on how to improve Bobcat? Raise an issue and describe your proposal, including why you think it would make a great addition to the framework. Set the label to `feature`.

## Contributing code

Maybe not only you have a cool idea but felt like moving it right away into the code? Or you saw an issue labeled `help wanted` and felt that you'd give a try? Even more awesome!

Fork the repository, create a branch, hack away your improvement and submit a pull request!

There are few things you need to keep in mind though.

### Description

Describe your changes in the pull request. Also, make your commit messages speak for themselves (you can read more about that [here](https://chris.beams.io/posts/git-commit/).

### Styleguide

Use [Google Style][google-java-style] with following modifications:

1. line width: 100
2. preserve newlines (short lines may be more readable)
3. `@formatter:off` and `@formatter:on` disables and enables formatter
4. use eclipse imports order ([instructions for IntelliJ][eclipse-imports-order-in-intellij])

![imports order settings for IntelliJ][eclipse-imports-order-in-intellij-img]

#### Eclipse Formatter XML file

We provide [XML file for JAVA format][formatter-xml] that may be imported into Eclipse and IntelliJ IDEA.

[google-java-style]: https://google.github.io/styleguide/javaguide.html
[eclipse-imports-order-in-intellij]: http://stackoverflow.com/questions/14716283/is-it-possible-for-intellij-to-organize-imports-the-same-way-as-in-eclipse
[eclipse-imports-order-in-intellij-img]: contributing/intellij-imports-order.png
[formatter-xml]: contributing/eclipse-java-bobcat-style.xml

### Testing

It would be really great if your changes would include tests checking them. They can be either unit tests or integration tests - where to put them?

- unit tests: in the `test` folder in the module they are testing
- integration tests: in the `bb-integration-tests` module, in the module-specific package, e.g. `com.cognifide.qa.bb.core` for Core module tests, `com.cognifide.qa.bb.proxy` for Traffic module tests, etc.

To build and test your code against existing suites, simply run `mvn clean test` from the project root.

>Unfortunately, due to the license, we cannot expose our AEM test suites, so if you would like to introduce a change related to that part of the framework, reach out to us and we will discuss a proper approach.

### Documentation

Every feature should be covered in Bobcat's documentation. If you have modified an existing code, please adjust the documentation accordingly.

In addition to the above, please make sure all classes and public methods have Javadocs.

