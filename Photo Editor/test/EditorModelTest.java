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
import model.FlipType;
import model.Greyscale;
import model.EditorImage;
import model.EditorImageImpl;
import model.PhotoEditorModel;
import model.PhotoEditorModelImpl;
import model.PixelRGB;
import model.PixelRGBImpl;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * A test class for model.
 */
public class EditorModelTest {
  private PhotoEditorModelEnhanced model;

  @Before
  public void setup() {
    this.model = new PhotoEditorModelImpl();
    OutputStream outStream = new ByteArrayOutputStream();
    PhotoEditorView view = new PhotoEditorViewImpl(new PrintStream(outStream));

    InputStream inputStream = new ByteArrayInputStream(
            ("load res/BaseImage.ppm test").getBytes(StandardCharsets.UTF_8));
    Readable input = new InputStreamReader(inputStream);
    PhotoEditorController c = new PhotoEditorControllerImpl(input, model, view);
    c.runController();
  }

  /**
   * tests getImageWidth and getImageHeight.
   */
  @Test
  public void testWidthHeight() {
    assertEquals(model.getImageWidth("test"), 403);
    assertEquals(model.getImageHeight("test"), 302);
  }

  /**
   * Tests getImageWidth failing when file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testWidthFail() {
    model.getImageWidth("fake");
  }

  /**
   * Tests getImageHeight failing when file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testHeightFail() {
    model.getImageHeight("fake");
  }

  /**
   * Tests brighten with positive number.
   * Note this also tests when some pixels go above max (255 for this purpose).
   */
  @Test
  public void testBrighten() {
    model.brighten("test", "testbrighten", 100);


    EditorImage imageBefore = new EditorImageImpl("res/BaseImage.ppm");
    EditorImage imageAfter = new EditorImageImpl("res/BrightenImageUp.ppm");

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testbrighten").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      int tempValRed = tempPixel.getRed();
      int tempValGreen = tempPixel.getGreen();
      int tempValBlue = tempPixel.getBlue();

      tempPixel.setRed(tempValRed + 100);
      tempPixel.setGreen(tempValGreen + 100);
      tempPixel.setBlue(tempValBlue + 100);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());

      //Reset values in case the same spot is chosen again
      tempPixel.setRed(tempValRed);
      tempPixel.setGreen(tempValGreen);
      tempPixel.setBlue(tempValBlue);
    }
  }

  /**
   * Tests brighten with negative number.
   * Note this also tests when some pixels go below min (0 for this purpose).
   */
  @Test
  public void testBrightenDark() {
    model.brighten("test", "testbrighten", -100);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testbrighten").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      int tempValRed = tempPixel.getRed();
      int tempValGreen = tempPixel.getGreen();
      int tempValBlue = tempPixel.getBlue();

      tempPixel.setRed(tempValRed - 100);
      tempPixel.setGreen(tempValGreen - 100);
      tempPixel.setBlue(tempValBlue - 100);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());

      //Reset values in case the same spot is chosen again
      tempPixel.setRed(tempValRed);
      tempPixel.setGreen(tempValGreen);
      tempPixel.setBlue(tempValBlue);
    }
  }

  /**
   * Tests brighten failing when file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBrightenFail() {
    model.brighten("DoesNotExist!", "test", 100);
  }

  /**
   * Tests greyscale with red.
   */
  @Test
  public void testGreyscaleRed() {
    model.greyscale("test", "testgrey", Greyscale.Red);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getRed(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getRed(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests greyscale with green.
   */
  @Test
  public void testGreyscaleGreen() {
    model.greyscale("test", "testgrey", Greyscale.Green);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      assertEquals(tempPixel.getGreen(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests greyscale with blue.
   */
  @Test
  public void testGreyscaleBlue() {
    model.greyscale("test", "testgrey", Greyscale.Blue);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      assertEquals(tempPixel.getBlue(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests greyscale with intensity.
   */
  @Test
  public void testGreyscaleIntensity() {
    model.greyscale("test", "testgrey", Greyscale.Intensity);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      int newVal = (int) (tempPixel.getRed() * 1.0 / 3.0
              + tempPixel.getGreen() * 1.0 / 3.0
              + tempPixel.getBlue() * 1.0 / 3.0);

      assertTrue(Math.abs(newVal - tempPixelAfter.getRed()) <= 1);
      assertTrue(Math.abs(newVal - tempPixelAfter.getGreen()) <= 1);
      assertTrue(Math.abs(newVal - tempPixelAfter.getBlue()) <= 1);
    }
  }

  /**
   * Tests greyscale with luma.
   */
  @Test
  public void testGreyscaleLuma() {
    model.greyscale("test", "testgrey", Greyscale.Luma);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      int newVal = (int) (tempPixel.getRed() * 0.2126
              + tempPixel.getGreen() * 0.7152
              + tempPixel.getBlue() * 0.0722);

      assertEquals(newVal, tempPixelAfter.getRed());
      assertEquals(newVal, tempPixelAfter.getGreen());
      assertEquals(newVal, tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests greyscale with value.
   */
  @Test
  public void testGreyscaleValue() {
    model.greyscale("test", "testgrey", Greyscale.Value);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testgrey").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];


      int newVal = tempPixel.getRed();

      if (newVal < tempPixel.getGreen()) {
        newVal = tempPixel.getGreen();
      }

      if (newVal < tempPixel.getBlue()) {
        newVal = tempPixel.getBlue();
      }

      assertEquals(newVal, tempPixelAfter.getRed());
      assertEquals(newVal, tempPixelAfter.getGreen());
      assertEquals(newVal, tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests greyscale failing when file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGreyscaleFail() {
    model.greyscale("DoesNotExist!", "test", Greyscale.Red);

    System.out.println("Should not reach");
  }

  /**
   * Tests flip horizontally.
   */
  @Test
  public void testFlipHorizontal() {
    model.flip("test", "testflip", FlipType.Horizontal);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testflip").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[randomI][pixelsBefore[randomI].length - (randomJ + 1)];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests flip vertically.
   */
  @Test
  public void testFlipVertical() {
    model.flip("test", "testflip", FlipType.Vertical);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testflip").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixel = pixelsBefore[pixelsBefore.length - (randomI + 1)][randomJ];
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests flip failing when file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFlipFail() {
    PhotoEditorModel model = new PhotoEditorModelImpl();
    model.flip("DoesNotExist!", "test", FlipType.Horizontal);

    System.out.println("Should not reach");
  }

  /**
   * Tests Pixel failing when max is not greater than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPixelFail() {
    PixelRGB pixel = new PixelRGBImpl(120, 120, 120, 0);

    System.out.println("Should not reach");
  }

  /**
   * Master test method for Pixel.
   */
  @Test
  public void testPixel() {
    PixelRGB pixel = new PixelRGBImpl(75, 12, 4, 90);

    assertEquals(pixel.getRed(), 75);
    assertEquals(pixel.getGreen(), 12);
    assertEquals(pixel.getBlue(), 4);

    pixel.setRed(50);
    pixel.setGreen(50);
    pixel.setBlue(50);

    assertEquals(pixel.getRed(), 50);
    assertEquals(pixel.getGreen(), 50);
    assertEquals(pixel.getBlue(), 50);

    //Should drop ALL values to 90
    pixel.setRed(255);
    pixel.setGreen(255);
    pixel.setBlue(255);

    assertEquals(pixel.getRed(), 90);
    assertEquals(pixel.getGreen(), 90);
    assertEquals(pixel.getBlue(), 90);

    //Should increase ALL values to 0
    pixel.setRed(-5);
    pixel.setGreen(-5);
    pixel.setBlue(-5);

    assertEquals(pixel.getRed(), 0);
    assertEquals(pixel.getGreen(), 0);
    assertEquals(pixel.getBlue(), 0);
  }

  /**
   * Tests Image failing when file given is not valid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadImage() {
    EditorImage image = new EditorImageImpl("DoesNotExist!");

    System.out.println("Should not reach");
  }

  /**
   * Tests image get width, height, and max.
   */
  @Test
  public void testImageBasic() {
    EditorImage image = new EditorImageImpl("res/BaseImage.ppm");

    assertEquals(image.getHeight(), 302);
    assertEquals(image.getWidth(), 403);
    assertEquals(image.getMax(), 255);
  }

  /**
   * Tests both image constructors for comparisons of getData and toString.
   */
  @Test
  public void testImageConstructors() {
    EditorImage image = new EditorImageImpl("res/BaseImage.ppm");
    EditorImage imageNew = new EditorImageImpl(image.getData(), 255);
    EditorImage imageNewMax = new EditorImageImpl(image.getData(), 500);

    assertEquals(imageNewMax.getHeight(), 302);
    assertEquals(imageNewMax.getWidth(), 403);
    assertEquals(imageNewMax.getMax(), 500);

    assertArrayEquals(image.getData(), imageNewMax.getData());
    assertNotEquals(image.toString(), imageNewMax.toString());
    assertEquals(image.toString(), imageNew.toString());
  }

  /**
   * Tests color transform sepia.
   */
  @Test
  public void testColorTransformSepia() {
    double[][] arr = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };

    model.filter("test", "testSepia", arr);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testSepia").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);

      int red = (int) (arr[0][0] * pixelsBefore[randomI][randomJ].getRed() + arr[0][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[0][2] * pixelsBefore[randomI][randomJ].getBlue());
      int green = (int) (arr[1][0] * pixelsBefore[randomI][randomJ].getRed() + arr[1][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[1][2] * pixelsBefore[randomI][randomJ].getBlue());
      int blue = (int) (arr[2][0] * pixelsBefore[randomI][randomJ].getRed() + arr[2][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[2][2] * pixelsBefore[randomI][randomJ].getBlue());

      PixelRGB tempPixelAfter = new PixelRGBImpl(red, green, blue, 255);

      PixelRGB tempPixel = new PixelRGBImpl(red, green, blue, 255);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests color transform greyscale.
   */
  @Test
  public void testColorTransformGreyscale() {
    double[][] arr = {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };

    model.filter("test", "testGreyscale", arr);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testGreyscale").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);

      int red = (int) (arr[0][0] * pixelsBefore[randomI][randomJ].getRed() + arr[0][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[0][2] * pixelsBefore[randomI][randomJ].getBlue());
      int green = (int) (arr[1][0] * pixelsBefore[randomI][randomJ].getRed() + arr[1][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[1][2] * pixelsBefore[randomI][randomJ].getBlue());
      int blue = (int) (arr[2][0] * pixelsBefore[randomI][randomJ].getRed() + arr[2][1] *
              pixelsBefore[randomI][randomJ].getGreen()
              + arr[2][2] * pixelsBefore[randomI][randomJ].getBlue());

      PixelRGB tempPixelAfter = new PixelRGBImpl(red, green, blue, 255);

      PixelRGB tempPixel = new PixelRGBImpl(red, green, blue, 255);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests filter failing when array is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testTransformFailNull() {
    double[][] arr = null;

    model.colorTransformation("test", "testFilterBlur", arr);
  }

  /**
   * Tests filter failing when array is not 3x3.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testTransformFailArrayBad() {
    double[][] arr = {{2, 2}, {2, 2}};

    model.colorTransformation("test", "testFilterBlur", arr);
  }

  /**
   * Tests filter for Blur.
   */
  @Test
  public void testFilterBlur() {
    double[][] arr = new double[][]{
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}
    };

    model.filter("test", "testFilterBlur", arr);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testFilterBlur").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];
      double red = 0;
      double green = 0;
      double blue = 0;

      for (int k = 0; k < arr.length; k++) {
        for (int m = 0; m < arr[0].length; m++) {
          int offX = k - (arr.length - 1) / 2;
          int offY = m - (arr.length - 1) / 2;

          if (!(randomI + offX < 0 || randomI + offX >= 302
                  || randomJ + offY < 0 || randomJ + offY >= 403)) {
            red += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getRed();
            blue += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getBlue();
            green += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getGreen();
          }
        }
      }

      PixelRGB tempPixel = new PixelRGBImpl((int) red, (int) green, (int) blue, 255);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests filter for Sharpen.
   */
  @Test
  public void testFilterSharpen() {
    double[][] arr = {{-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, -0.125, -0.125, -0.125, -0.125}
    };

    model.filter("test", "testFilterBlur", arr);

    PixelRGB[][] pixelsBefore = model.getImage("test").getData();
    PixelRGB[][] pixelsAfter = model.getImage("testFilterBlur").getData();

    //Tests random pixels to see if they are changed as they should be
    for (int i = 0; i < 100; i++) {
      int randomI = (int) (Math.random() * 302);
      int randomJ = (int) (Math.random() * 403);
      PixelRGB tempPixelAfter = pixelsAfter[randomI][randomJ];
      double red = 0;
      double green = 0;
      double blue = 0;

      for (int k = 0; k < arr.length; k++) {
        for (int m = 0; m < arr[0].length; m++) {
          int offX = k - (arr.length - 1) / 2;
          int offY = m - (arr.length - 1) / 2;

          if (!(randomI + offX < 0 || randomI + offX >= 302
                  || randomJ + offY < 0 || randomJ + offY >= 403)) {
            red += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getRed();
            blue += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getBlue();
            green += arr[k][m] * pixelsBefore[randomI + offX][randomJ + offY].getGreen();
          }
        }
      }

      PixelRGB tempPixel = new PixelRGBImpl((int) red, (int) green, (int) blue, 255);

      assertEquals(tempPixel.getRed(), tempPixelAfter.getRed());
      assertEquals(tempPixel.getGreen(), tempPixelAfter.getGreen());
      assertEquals(tempPixel.getBlue(), tempPixelAfter.getBlue());
    }
  }

  /**
   * Tests filter failing when array is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFilterFailArrayNull() {
    double[][] arr = null;

    model.filter("test", "testFilterBlur", arr);
  }

  /**
   * Tests filter failing when array is even dimensions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFilterFailArrayEven() {
    double[][] arr = {{2, 2}, {2, 2}};

    model.filter("test", "testFilterBlur", arr);
  }

  /**
   * Tests filter failing when array is not a square.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFilterFailArrayRect() {
    double[][] arr = {{2, 2, 2, 2, 2}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 2}};

    model.filter("test", "testFilterBlur", arr);
  }
}

