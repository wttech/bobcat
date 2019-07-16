---
title: "Module: Analytics"
---

## About the module
Analytics module allows to verify the analytics implementations which are based on the datalayer approach.
It supports the Adobe Analytics and the Google Analytics solutions.

The main idea here is to compare the existing datalayer with the expected one. 
For this comparison we're using the lenient mode, which means that we're checking if the expected objects are present in the actual datalayer. So the actual datalayer may contain more data, but as long as the expected tags and their's values are present there, our test will pass.


## Getting started

To get the Analytics module to be ready to use, these three steps needs to be followed:

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
    It is done within the test, where the AdobeAnalytics class or the GoogleAnalytics class should be injected.
    Go to the [First Analytics Test]({{site.baseurl}}/docs/guides/first-analytics-test/) guide to see an example.
    
    Also, depending on the project implementation, the datalayer may have it's own unique name, therefore this name also needs to be defined for our tests setup.
    To do so, add the configuration yaml file, where the Adobe or Google property will be defined:
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
     so if it's covers your's project implementation, you may skip this config.
 
## Adobe Analytics, Google Analytics... What's the difference? 

At this point you might ask yourself why do I need to define which analytics approach I will be testing. In both cases we are checking the data layer, right? So what's the point?

Well, the thing is that the structure of the Adobe Analytics and the Google Analytics data layer is different. 
In the Adobe Analytics datalayer implementation we have one object with it's sub-objects while in the Google Analytics we have an array with many objects.

1. Adobe Analytics
    In the Adobe Analytics the datalayer is the one root object with the sub-objects. 
    New analytics event updates the datalayer and the new values are overwriting the old ones.
    
    For example the actual datalayer may look like that:
    ```json
       {
         "assetInfo": {},
         "eventInfo": {
           "eventLabel": "pageview",
           "eventType": "pageload"
         },
         "pageInfo": {
           "language": "en_us",
           "pageName": "Partner Finder",
           "siteName": "spp"
         },
         "partnerInfo": {},
         "searchInfo": {},
         "userInfo": {
           "loginStatus": "NotSignedIn"
         }
       }
     ```

    The expected datalayer may be defined as follows:
    ```json
        {
         "eventInfo": {
           "eventLabel": "pageview",
           "eventType": "pageload"
         },
         "pageInfo": {
           "language": "en_us",
           "pageName": "Partner Finder",
           "siteName": "spp"
         },
         "userInfo": {
           "loginStatus": "NotSignedIn"
         }
        }
     ```

2. Google Analytics
    In case of the Google Analytics the data is stored in an array. Besides the data that our implementation is adding, a lot of other things is happening there. 
    Part of this data is added automatically by the Google. Additionally, every analytics event adds new data to this array. 
    After few innocent click our datalayer may be really huge.
    
    A very simple datalayer may look like that:
    ```json
         [
           {
             "OnetrustActiveGroups": ",102,1,103,2,3,0_173540,109,4,0_173551,0_173681,0_173680,"
           },
           {
             "OptanonActiveGroups": ",102,1,103,2,3,0_173540,109,4,0_173551,0_173681,0_173680,"
           },
           {
             "pageName": "Home",
             "pageURL": "https://www.cognifide.com/",
             "userID": "GA1.2.923483004.1563177080"
           },
           {
             "event": "gtm.dom",
             "gtm.uniqueEventId": 1
           },
           {
              "event": "gtm.load",
              "gtm.uniqueEventId": 3
            }
         ]
     ```
    
    To test your analytics implementation your will rather exclude the automatically generated data and define the expected datalayer like this:
    ```json
         [
           {
             "pageName": "Home",
             "pageURL": "https://www.cognifide.com/"
           }
         ]
     ```

## What can and what cannot be tested

Analytics module allows in fast and easy way create tests of the Adobe Analytics or the Google Analytics.
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
