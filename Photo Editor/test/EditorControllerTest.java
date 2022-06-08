import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import controller.PhotoEditorController;
import controller.PhotoEditorControllerImpl;
import model.assignmentfive.PhotoEditorModelEnhanced;
import model.MockModel;
import model.PhotoEditorModel;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * A test class for controller.
 */
public class EditorControllerTest {
  private PhotoEditorView view;
  private PhotoEditorModel model;
  private OutputStream outStream;
  private StringBuilder log;

  @Before
  public void setup() {
    OutputStream outStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    PhotoEditorModelEnhanced model = new MockModel(log);

    this.view = new PhotoEditorViewImpl(new PrintStream(outStream));
    this.model = model;
    this.outStream = outStream;
    this.log = log;
  }


  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    InputStream inputStream =
            new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);


    PhotoEditorController test = new PhotoEditorControllerImpl(input, null, this.view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    InputStream inputStream =
            new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);


    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    PhotoEditorController test = new PhotoEditorControllerImpl(null, this.model, this.view);
  }

  @Test
  public void testQuit() {
    InputStream inputStream =
            new ByteArrayInputStream("quit more_stuff".getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("Thank you for using the photo editor!", this.outStream.toString());
  }

  @Test
  public void testNotACommand() {
    InputStream inputStream =
            new ByteArrayInputStream("not_a_command".getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("That is not a valid command\nThank you for using the photo editor!",
            this.outStream.toString());
  }

  @Test
  public void testBrighten() {
    InputStream inputStream = new ByteArrayInputStream(
            "brighten hi hi hi brighten frog frog_bright 4 quit".getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("Those inputs do not work for the brighten command\n" +
            "frog has been brightened by 4 and saved as frog_bright\n" +
            "Thank you for using the photo editor!", this.outStream.toString());
    assertEquals("imageName: frog destName: frog_bright inc: 4\n", this.log.toString());
  }

  @Test
  public void testFlip() {
    InputStream inputStream = new ByteArrayInputStream(("horizontal-flip frog frog_horz" +
            " vertical-flip frog frog_vert").getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("frog has been flipped horizontally and saved as frog_horz\n" +
            "frog has been flipped vertically and saved as frog_vert\n" +
            "Thank you for using the photo editor!", this.outStream.toString());
    assertEquals("imageName: frog destName: frog_horz FlipType: horizontal\n" +
            "imageName: frog destName: frog_vert FlipType: vertical\n", this.log.toString());
  }

  @Test
  public void testGreyScale() {
    InputStream inputStream = new ByteArrayInputStream(
            ("red-component frog frog_red green-component frog frog_green " +
                    "blue-component frog frog_blue value-component frog frog_value " +
                    "intensity-component frog frog_intensity luma-component frog frog_luma")
                    .getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("frog has been greyscaled based on red and saved as frog_red\n" +
            "frog has been greyscaled based on green and saved as frog_green\n" +
            "frog has been greyscaled based on blue and saved as frog_blue\n" +
            "frog has been greyscaled based on value and saved as frog_value\n" +
            "frog has been greyscaled based on intensity and saved as frog_intensity\n" +
            "frog has been greyscaled based on luma and saved as frog_luma\n" +
            "Thank you for using the photo editor!", this.outStream.toString());

    assertEquals("imageName: frog destName: frog_red FlipType: red\n" +
            "imageName: frog destName: frog_green FlipType: green\n" +
            "imageName: frog destName: frog_blue FlipType: blue\n" +
            "imageName: frog destName: frog_value FlipType: value\n" +
            "imageName: frog destName: frog_intensity FlipType: intensity\n" +
            "imageName: frog destName: frog_luma FlipType: luma\n", this.log.toString());
  }

  @Test
  public void testColorTransform() {
    InputStream inputStream = new ByteArrayInputStream(
            ("sepia frog frog_sepia greyscale frog frog_gt")
                    .getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("frog has been color transformed and saved as frog_sepia\n" +
            "frog has been color transformed and saved as frog_gt\n" +
            "Thank you for using the photo editor!", this.outStream.toString());

    assertEquals("imageName: frog destName: frog_sepia"
            + "imageName: frog destName: frog_gt", this.log.toString());
  }

  @Test
  public void testFilter() {
    InputStream inputStream = new ByteArrayInputStream(
            ("blur frog frog_blur" + " sharpen frog frog_sharpen")
                    .getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);

    PhotoEditorController test = new PhotoEditorControllerImpl(input, this.model, this.view);
    test.runController();
    assertEquals("frog has been filtered and saved as frog_blur\n" +
            "frog has been filtered and saved as frog_sharpen\n" +
            "Thank you for using the photo editor!", this.outStream.toString());

    assertEquals("imageName: frog destName: frog_blur"
            + "imageName: frog destName: frog_sharpen", this.log.toString());
  }
}
