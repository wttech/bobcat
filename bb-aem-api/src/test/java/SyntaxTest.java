/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
import static com.cognifide.qa.bb.api.syntax.Assertions.seeThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import com.cognifide.qa.bb.aem.api.actions.Edit;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.Bobcat;
import com.cognifide.qa.bb.modules.CoreModule;
import com.google.inject.Guice;

public class SyntaxTest {

  @Test
  public void apiSyntaxCompiles() {

    Actor author = Guice.createInjector(new CoreModule()).getInstance(Bobcat.class);

    //    author.attemptsTo(Navigate.to("https://google.com"));

    author.attemptsTo(Edit.component(DummyComponent.class));

    author.should(seeThat(new ExampleState(), is("TEST")));
  }
}
