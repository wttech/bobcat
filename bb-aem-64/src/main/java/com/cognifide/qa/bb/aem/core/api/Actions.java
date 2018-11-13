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

public class Actions {
  public class Siteadmin {
    public static final String CREATE_PAGE = "pageCreateAction";
  }


  public class Component {
    public static final String EDIT = "editComponentAction";
    public static final String CONFIGURE = "configureComponentAction";
  }


  public class Page {
    public static final String CREATE = "createAction";
    public static final String DELETE = "deleteAction";
  }


  public class Login {

    public static final String LOG_IN = "logInAction";
    public static final String LOG_OUT = "logOutAction";
  }
}
