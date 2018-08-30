import com.cognifide.qa.bb.aem.api.Dialog;

public class DummyDialog implements Dialog {
  @Override
  public void close() {
    System.out.println("Closing dialog");
  }

  @Override
  public boolean isClosed() {
    System.out.println("Verifying that dialog is closed");
    return true;
  }
}
