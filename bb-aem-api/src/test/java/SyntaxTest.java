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
