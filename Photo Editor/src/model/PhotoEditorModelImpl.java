package model;

import java.util.HashMap;
import java.util.Map;

import model.assignmentfive.PhotoEditorModelEnhanced;

/**
 * Implementation of the photo editor. Has set of operations and can load/save multiple photos.
 */
public class PhotoEditorModelImpl implements PhotoEditorModelEnhanced {
  private Map<String, EditorImage> images;

  /**
   * Default constructor.
   */
  public PhotoEditorModelImpl() {
    images = new HashMap<String, EditorImage>();
  }

  /**
   * Gets the width of this model's image.
   *
   * @return The width of the image
   * @throws IllegalArgumentException if image does not exist
   */
  @Override
  public int getImageWidth(String imageName) throws IllegalArgumentException {
    EditorImage image = images.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image Null");
    }
    return image.getWidth();
  }

  /**
   * Gets the height of this model's image.
   *
   * @return The height of the image
   * @throws IllegalArgumentException if image does not exist
   */
  @Override
  public int getImageHeight(String imageName) throws IllegalArgumentException {
    EditorImage image = images.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image Null");
    }
    return image.getHeight();
  }

  /**
   * Brightens the given image by the given increment, then save it to the given destination.
   * If the increment is negative, will darken the image.
   *
   * @param imageName The name of the image to brighten
   * @param destName  The location to store the image to
   * @param inc       The increment to brighten the image by
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  @Override
  public void brighten(String imageName, String destName, int inc) throws IllegalArgumentException {
    EditorImage image = getImage(imageName);

    PixelRGB[][] copyPixels = image.getData();
    //Java is pass by reference, so we must create a deep copy of pixel data as to not alter
    //original photos data
    PixelRGB[][] newPixels = new PixelRGBImpl[image.getHeight()][image.getWidth()];

    for (int i = 0; i < newPixels.length; i++) {
      for (int j = 0; j < newPixels[i].length; j++) {
        int newRed = copyPixels[i][j].getRed() + inc;
        int newGreen = copyPixels[i][j].getGreen() + inc;
        int newBlue = copyPixels[i][j].getBlue() + inc;

        newPixels[i][j] = new PixelRGBImpl(newRed, newGreen, newBlue, image.getMax());
      }
    }

    EditorImage returnImage = new EditorImageImpl(newPixels, image.getMax());

    images.put(destName, returnImage);
  }

  /**
   * Greyscales the given image based on the given Greyscale, then saves it to the given
   * destination.
   *
   * @param imageName The name of the image to greyscale
   * @param destName  The location to store the image to
   * @param type      The type of greyscale to perform
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  @Override
  public void greyscale(String imageName, String destName, Greyscale type)
          throws IllegalArgumentException {

    EditorImage image = getImage(imageName);
    EditorImage returnImage = null;

    switch (type) {
      case Red:
        returnImage = startGreyscale(1.0, 0.0, 0.0, image);
        break;
      case Green:
        returnImage = startGreyscale(0.0, 1.0, 0.0, image);
        break;
      case Blue:
        returnImage = startGreyscale(0.0, 0.0, 1.0, image);
        break;
      case Value:
        returnImage = startValueGreyscale(image);
        break;
      case Intensity:
        returnImage = startGreyscale(1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, image);
        break;
      case Luma:
        returnImage = startGreyscale(0.2126, 0.7152, 0.0722, image);
        break;
      default:
        //do nothing
    }

    images.put(destName, returnImage);

  }

  private EditorImageImpl startGreyscale(double a, double b, double c, EditorImage image) {
    PixelRGB[][] copyPixels = image.getData();
    //Java is pass by reference, so we must create a deep copy of pixel data as to not alter
    //original photos data
    PixelRGB[][] newPixels = new PixelRGBImpl[image.getHeight()][image.getWidth()];

    for (int i = 0; i < newPixels.length; i++) {
      for (int j = 0; j < newPixels[i].length; j++) {

        int greyScaleValue = (int) (a * copyPixels[i][j].getRed()
                + b * copyPixels[i][j].getGreen()
                + c * copyPixels[i][j].getBlue());

        newPixels[i][j] = new PixelRGBImpl(greyScaleValue, greyScaleValue,
                greyScaleValue, image.getMax());
      }
    }

    return new EditorImageImpl(newPixels, image.getMax());
  }

  //Value works differently than the others, so it has its own method
  private EditorImageImpl startValueGreyscale(EditorImage image) {
    PixelRGB[][] copyPixels = image.getData();
    //Java is pass by reference, so we must create a deep copy of pixel data as to not alter
    //original photos data
    PixelRGB[][] newPixels = new PixelRGBImpl[image.getHeight()][image.getWidth()];

    for (int i = 0; i < newPixels.length; i++) {
      for (int j = 0; j < newPixels[i].length; j++) {
        int greyScaleValue = copyPixels[i][j].getRed();

        if (greyScaleValue < copyPixels[i][j].getGreen()) {
          greyScaleValue = copyPixels[i][j].getGreen();
        }

        if (greyScaleValue < copyPixels[i][j].getBlue()) {
          greyScaleValue = copyPixels[i][j].getBlue();
        }

        newPixels[i][j] = new PixelRGBImpl(greyScaleValue, greyScaleValue,
                greyScaleValue, image.getMax());
      }
    }

    return new EditorImageImpl(newPixels, image.getMax());
  }

  /**
   * Flips the given image based on the given FlipType, then saves it to the given destination.
   *
   * @param imageName The name of the image to flip
   * @param destName  The location to store the image to
   * @param type      The type of flip to perform
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  @Override
  public void flip(String imageName, String destName, FlipType type)
          throws IllegalArgumentException {

    EditorImage image = getImage(imageName);
    EditorImage returnImage = null;
    switch (type) {
      case Horizontal:
        returnImage = horizontalFlip(image);
        break;
      case Vertical:
        returnImage = verticalFlip(image);
        break;
      default:
        //do nothing
    }
    images.put(destName, returnImage);
  }

  private EditorImage horizontalFlip(EditorImage image) {
    PixelRGB[][] copyPixels = image.getData();
    //Java is pass by reference, so we must create a deep copy of pixel data as to not alter
    //original photos data
    PixelRGB[][] newPixels = new PixelRGBImpl[image.getHeight()][image.getWidth()];

    for (int i = 0; i < newPixels.length; i++) {
      for (int j = 0; j < newPixels[i].length; j++) {
        PixelRGB tempPixel = copyPixels[i][newPixels[i].length - (j + 1)];

        newPixels[i][j] = new PixelRGBImpl(tempPixel.getRed(), tempPixel.getGreen(),
                tempPixel.getBlue(), image.getMax());
      }
    }
    return new EditorImageImpl(newPixels, image.getMax());
  }

  private EditorImage verticalFlip(EditorImage image) {
    PixelRGB[][] copyPixels = image.getData();
    //Java is pass by reference, so we must create a deep copy of pixel data as to not alter
    //original photos data
    PixelRGB[][] newPixels = new PixelRGBImpl[image.getHeight()][image.getWidth()];

    for (int i = 0; i < newPixels.length; i++) {
      for (int j = 0; j < newPixels[i].length; j++) {
        PixelRGB tempPixel = copyPixels[newPixels.length - (i + 1)][j];

        newPixels[i][j] = new PixelRGBImpl(tempPixel.getRed(), tempPixel.getGreen(),
                tempPixel.getBlue(), image.getMax());
      }
    }
    return new EditorImageImpl(newPixels, image.getMax());
  }

  @Override
  public EditorImage getImage(String imageName) throws IllegalArgumentException {
    EditorImage returnImage = images.get(imageName);

    if (returnImage == null) {
      throw new IllegalArgumentException("Invalid Image Name");
    }

    return returnImage;
  }

  @Override
  public void putImage(String dest, EditorImage im) throws IllegalArgumentException {
    if (im == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }

    images.put(dest, im);
  }

  @Override
  public void colorTransformation(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    if (arr == null) {
      throw new IllegalArgumentException("Array cannot be null");
    } else if (arr.length != 3 || arr[0].length != 3 || arr[1].length != 3 || arr[2].length != 3) {
      throw new IllegalArgumentException("Array must be 3x3");
    }


    EditorImage im = getImage(imageName);
    PixelRGB[][] oldPixels = im.getData();
    PixelRGB[][] newPixels = new PixelRGB[im.getHeight()][];

    for (int i = 0; i < im.getHeight(); i++) {
      PixelRGB[] row = new PixelRGB[im.getWidth()];
      for (int j = 0; j < im.getWidth(); j++) {
        int red = (int) (arr[0][0] * oldPixels[i][j].getRed() + arr[0][1] *
                oldPixels[i][j].getGreen() + arr[0][2] * oldPixels[i][j].getBlue());
        int green = (int) (arr[1][0] * oldPixels[i][j].getRed() + arr[1][1] *
                oldPixels[i][j].getGreen() + arr[1][2] * oldPixels[i][j].getBlue());
        int blue = (int) (arr[2][0] * oldPixels[i][j].getRed() + arr[2][1] *
                oldPixels[i][j].getGreen() + arr[2][2] * oldPixels[i][j].getBlue());

        row[j] = new PixelRGBImpl(red, green, blue, im.getMax());
      }
      newPixels[i] = row;
    }

    EditorImage newIm = new EditorImageImpl(newPixels, im.getMax());
    images.put(destName, newIm);
  }

  /**
   * Filters the image based on the given array, then saves it to the given destination.
   *
   * @param imageName The name of the image to be colorTransformed
   * @param destName  The key to map the image to
   * @param arr       The kernel to filter by
   * @throws IllegalArgumentException If the imageName does not correspond to an image, or any value
   *                                  in the array is null
   */
  @Override
  public void filter(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    if (arr == null) {
      throw new IllegalArgumentException("Array cannot be null");
    } else if (arr.length != arr[0].length || arr.length % 2 == 0 || arr[0].length % 2 == 0) {
      throw new IllegalArgumentException("Array must be a square and have have odd dimensions");
    }

    EditorImage im = getImage(imageName);
    PixelRGB[][] oldPixels = im.getData();
    PixelRGB[][] newPixels = new PixelRGB[im.getHeight()][];

    for (int i = 0; i < im.getHeight(); i++) {
      PixelRGB[] row = new PixelRGB[im.getWidth()];
      for (int j = 0; j < im.getWidth(); j++) {

        double red = 0;
        double green = 0;
        double blue = 0;

        for (int k = 0; k < arr.length; k++) {
          for (int m = 0; m < arr[0].length; m++) {
            int offX = k - (arr.length - 1) / 2;
            int offY = m - (arr.length - 1) / 2;

            if (!(i + offX < 0 || i + offX >= im.getHeight()
                    || j + offY < 0 || j + offY >= im.getWidth())) {
              red += arr[k][m] * oldPixels[i + offX][j + offY].getRed();
              blue += arr[k][m] * oldPixels[i + offX][j + offY].getBlue();
              green += arr[k][m] * oldPixels[i + offX][j + offY].getGreen();
            }
          }
        }

        row[j] = new PixelRGBImpl((int) red, (int) green, (int) blue, im.getMax());
      }
      newPixels[i] = row;
    }

    EditorImage newIm = new EditorImageImpl(newPixels, im.getMax());
    images.put(destName, newIm);

  }

  private boolean verifyLocation() {
    return true;
  }
}
