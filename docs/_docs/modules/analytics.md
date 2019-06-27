---
title: "Module: Analytics"
---

{% include under-construction.html %}

## Getting started

To get Analytics module to be ready to use follow these two steps:

1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-analytics', version: '<Bobcat Version>'
    }
    ```
2. Installation:
    
    To install this module add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.analytics.AnalyticsModule
     ```
3. Configuration
    
    Analytics module supports the Adobe Analytics as well as the Google Analytics solution. To make it work we need to define which solution has been implemented in our project (as there are some differences between those two approaches).
    More details you may find in the [First Analytics Test]({{site.baseurl}}/docs/guides/first-analytics-test/) guide.
    
    Also, depending on the project implementation, the datalayer may have it's own unique name, therefore this name also needs to be defined for our tests setup.
    To do so, add the configuration yaml file, where the Adobe or Google property needs to be defined:
     ```yaml
         adobe.analytics.datalayer: [name]
     ```
     or
     ```yaml
         google.analytics.datalayer: [name]
     ```
     The default values are defined as follows:
     ```yaml
         adobe.analytics.datalayer: digitalData
         google.analytics.datalayer: dataLayer
     ```
    
## What can and what cannot be tested

Analytics module allows in fast and easy way to create tests of the Adobe Analytics or the Google Analytics.
Both approaches are based on the comparison of actual and expected datalayer, processed as json files.

Such approach limits to the minimum the time of tests creation. What's probably even more important, tests (or at least the main part of the test) can be created by non-technical team members, e.g. by the Business Analysts.

This solution brings also some limitations, which in terms of analytics testing, should be considered:

- Analytics module supports the datalayer testing, therefore:
  - We can check if defined properties are present in the datalayer and have correct values
  - We can parametrize the properties value, so for example tests can be performed on different environments, even if the domain is added to the datalayer
  - We cannot verify that some property is not present in the datalayer
  - For Google Analyics we cannot verify that some unwanted objects have been added to the datalayer table
- Analytics requests testing is not supported by this module, therefore:
  - We cannot check if the expected request has been sent to the analytics
  - We cannot check if the requests sent to the analytics were not duplicated
