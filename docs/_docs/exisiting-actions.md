---
title: "Available actions"
---

Bobcat arrives with some already prepared actions that can be used in testing.

## Core Actions

  - | **CoreActions.CHECK_DISPLAYED_TITLE** |  action allows to check if displayed page has correct title |
     |Type: | Action with Data |
     |Data: | DisplayedTitleData - keeps title |
    | Response: | No |
     
## AEM Actions

  - | **AemActions.LOG_IN** |  action that login to author instance using parameters from configuration |
     |Type: | Action |     
    | Response: | No |
    
  - | **AemActions.LOG_OUT** |  action allows to log out from author instance|
     |Type: | Action |     
    | Response: | No |
    
  - | **AemActions.CREATE_PAGE_VIA_SLING** |  action allows to create page |
     |Type: | Action with Data |
     |Data: | SlingPageData - needs content path and prepared (using SlingDataXMLBuilder) values of page |
    | Response: | No |
    
  - | **AemActions.DELETE_PAGE_VIA_SLING** |  action allows to delete page |
     |Type: | Action with Data |
     |Data: | SlingPageData - requires only content path |
    | Response: | No |
    
  - | **AemActions.CONFIGURE_COMPONENT** |  action allows to configure component |
     |Type: | Action with Data |
     |Data: | ConfigureComponentData - component path, order, configuration data| 
    | Response: | No |

  - | **AemActions.CREATE_PAGE_VIA_SITEADMIN** |  action allows to check if displayed page has correct title |
     |Type: | Action with Data |
     |Data: | CreatePageActionData - template, data, page name |
    | Response: | No |
