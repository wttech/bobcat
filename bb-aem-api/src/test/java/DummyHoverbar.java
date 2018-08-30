import com.cognifide.qa.bb.aem.api.Hoverbar;

public class DummyHoverbar implements Hoverbar {

  @Override
  public void clickOption(String option) {
    System.out.println("Selecting option: " + option);
  }
}
