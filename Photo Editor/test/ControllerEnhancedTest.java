
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import controller.PhotoEditorController;
import controller.PhotoEditorControllerImpl;
import model.assignmentfive.PhotoEditorModelEnhanced;
import model.EditorImage;
import model.PhotoEditorModelImpl;
import model.PixelRGB;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test class for the enhanced controller.
 */
public class ControllerEnhancedTest {
  private PhotoEditorModelEnhanced model;
  private OutputStream outStream;

  private void setup(String in) {
    this.model = new PhotoEditorModelImpl();
    this.outStream = new ByteArrayOutputStream();
    PhotoEditorView view = new PhotoEditorViewImpl(new PrintStream(outStream));

    InputStream inputStream = new ByteArrayInputStream(
            (in).getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);
    PhotoEditorController c = new PhotoEditorControllerImpl(input, model, view);
    c.runController();
  }

  @Test
  public void testJPGConversion() {
    setup("load res/BaseImage.jpg jpg save res/BaseImage.png jpg save res/BaseImage.ppm " +
            "jpg save res/TestBaseImage.jpg jpg load res/BaseImage.ppm ppm load" +
            " res/BaseImage.png png");

    File ppmFile = new File("res/GreyscaleRed.ppm");
    assertTrue(ppmFile.exists());

    File pngFile = new File("res/GreyscaleRed.png");
    assertTrue(pngFile.exists());

    File jpgFile = new File("res/TestBaseImage.jpg");
    assertTrue(jpgFile.exists());

    //jpg is not tested for pixel equality because there is some loss of data when converting to
    //jpg, and therefore the pixels are not the same

    EditorImage ppm = model.getImage("ppm");
    EditorImage jpg = model.getImage("jpg");
    EditorImage png = model.getImage("png");

    PixelRGB[][] ppmData = ppm.getData();
    PixelRGB[][] jpgData = jpg.getData();
    PixelRGB[][] pngData = png.getData();

    for (int i = 0; i < ppm.getHeight(); i++) {
      for (int j = 0; j < ppm.getWidth(); j++) {
        assertEquals(jpgData[i][j].getBlue(),ppmData[i][j].getBlue());
        assertEquals(jpgData[i][j].getGreen(),ppmData[i][j].getGreen());
        assertEquals(jpgData[i][j].getRed(),ppmData[i][j].getRed());

        assertEquals(jpgData[i][j].getBlue(),pngData[i][j].getBlue());
        assertEquals(jpgData[i][j].getGreen(),pngData[i][j].getGreen());
        assertEquals(jpgData[i][j].getRed(),pngData[i][j].getRed());
      }
    }
  }

  /**
   * tests load failing with bad name.
   */
  @Test
  public void testLoadFail() {
    setup("load Fake jpg");

    assertEquals("Those inputs do not work for the load command\n" +
            "Thank you for using the photo editor!", outStream.toString());
  }

  /**
   * tests save failing with bad name.
   */
  @Test
  public void testSaveFail() {
    setup("save res/BaseImage.ppm fake");

    assertEquals("Those inputs do not work for the save command\n" +
            "Thank you for using the photo editor!", outStream.toString());
  }
}
