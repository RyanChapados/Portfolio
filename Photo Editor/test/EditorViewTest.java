import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * A test class for PhotoEditorView.
 */
public class EditorViewTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    PhotoEditorView test = new PhotoEditorViewImpl(null);
  }

  @Test(expected = IOException.class)
  public void testNullRender() throws IOException {
    File f = new File("");
    PrintStream p = new PrintStream(f);
    PhotoEditorView test = new PhotoEditorViewImpl(p);

    test.renderMessage("This should fail");
  }

  @Test
  public void testRenderMessage() throws IOException {
    OutputStream outStream = new ByteArrayOutputStream();
    PhotoEditorView test = new PhotoEditorViewImpl(new PrintStream(outStream));

    test.renderMessage("Hello there");
    String result = outStream.toString();

    assertEquals("Hello there", result);
  }
}
