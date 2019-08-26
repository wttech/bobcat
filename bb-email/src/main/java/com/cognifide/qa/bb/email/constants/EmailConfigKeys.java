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
package com.cognifide.qa.bb.email.constants;

public final class EmailConfigKeys {

  public static final String EMAIL_SERVER_ADDRESS = "email.${id}.server.address";

  public static final String EMAIL_SERVER_PORT = "email.${id}.server.port";

  public static final String EMAIL_SERVER_PROTOCOL = "email.${id}.server.protocol";

  public static final String EMAIL_USERNAME = "email.${id}.username";

  public static final String EMAIL_PASSWORD = "email.${id}.password";

  public static final String EMAIL_ADDRESS = "email.${id}.address";

  public static final String EMAIL_FOLDER_NAME = "email.${id}.folder.name";

  public static final String EMAIL_ADDRESS_PATTERN = "email.${id}.address.pattern";

  public static final String SMTP_SERVER_SECURE = "smtp.${id}.server.secure";

  public static final String SMTP_SERVER_PORT = "smtp.${id}.server.port";

  public static final String SMTP_SERVER_ADDRESS = "smtp.${id}.server.address";

  public static final String ID = "id";

  private EmailConfigKeys() {
  }

}
