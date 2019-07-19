/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.aem.core.api;

/**
 * Lists all {@link com.cognifide.qa.bb.api.actions.Action}s and {@link com.cognifide.qa.bb.api.actions.ActionWithData} that are registered in AEM 6.5 module.
 */
public class AemActions {

  private AemActions() {
    //empty
  }

  //login actions
  public static final String LOG_IN = "logIn";
  public static final String LOG_OUT = "logOut";

  //page actions
  public static final String CREATE_PAGE_VIA_SLING = "createPageViaSling";
  public static final String DELETE_PAGE_VIA_SLING = "deletePageViaSling";

  //component actions
  public static final String EDIT_COMPONENT = "editComponent";
  public static final String CONFIGURE_COMPONENT = "configureComponent";

  //siteadmin actions
  public static final String CREATE_PAGE_VIA_SITEADMIN = "createPageViaSiteadmin";
}
