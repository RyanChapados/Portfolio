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
import model.EditorImage;
import model.PhotoEditorModelImpl;
import model.PixelRGB;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the enhanced model.
 */
public class PhotoEditorModelEnhancedTest {
  private PhotoEditorModelEnhanced model;

  @Before
  public void setup() {
    this.model = new PhotoEditorModelImpl();
    OutputStream outStream = new ByteArrayOutputStream();
    PhotoEditorView view = new PhotoEditorViewImpl(new PrintStream(outStream));

    InputStream inputStream = new ByteArrayInputStream(
            ("load res/BaseImage.jpg jpg").getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);
    PhotoEditorController c = new PhotoEditorControllerImpl(input, model, view);
    c.runController();
  }

  /**
   * Tests colortransform.
   */
  @Test
  public void testColorTransform() {
    double[][] arr = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
    model.colorTransformation("jpg", "test", arr);

    EditorImage test = model.getImage("test");
    PixelRGB[][] testData = test.getData();
    PixelRGB[][] originalData = model.getImage("jpg").getData();

    for (int i = 0; i < test.getHeight(); i++) {
      for (int j = 0; j < test.getWidth(); j++) {
        int oldRed = originalData[i][j].getRed();
        int oldGreen = originalData[i][j].getGreen();
        int oldBlue = originalData[i][j].getBlue();
        int newRed = (int) (arr[0][0] * oldRed + arr[0][1] * oldGreen + arr[0][2] * oldBlue);
        int newGreen = (int) (arr[1][0] * oldRed + arr[1][1] * oldGreen + arr[1][2] * oldBlue);
        int newBlue = (int) (arr[2][0] * oldRed + arr[2][1] * oldGreen + arr[2][2] * oldBlue);

        if (newRed > 255) {
          newRed = 255;
        }
        if (newGreen > 255) {
          newGreen = 255;
        }
        if (newBlue > 255) {
          newBlue = 255;
        }

        assertEquals(newRed, testData[i][j].getRed());
        assertEquals(newBlue, testData[i][j].getBlue());
        assertEquals(newGreen, testData[i][j].getGreen());
      }
    }
  }

  /**
   * Tests colortransform fail when arr is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testColorTransformError() {
    model.colorTransformation("jpg", "test", null);
  }
}
