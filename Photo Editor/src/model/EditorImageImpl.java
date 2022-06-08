package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * implementation of PPMImage. Min value is 0.
 */
public class EditorImageImpl implements EditorImage {
  private int height;
  private int width;
  private int max;
  private PixelRGB[][] pixels;

  /**
   * Creates a new EditorImage from a given BufferedImage.
   * @param image The image to load from
   */
  public EditorImageImpl(BufferedImage image) {
    this.height = image.getHeight();
    this.width = image.getWidth();
    PixelRGB[][] pixels = new PixelRGB[this.height][];

    for (int i = 0; i < this.height; i++) {
      PixelRGB[] row = new PixelRGB[image.getWidth()];
      for (int j = 0; j < this.width; j++) {
        Color c = new Color(image.getRGB(j, i));

        row[j] = new PixelRGBImpl(c.getRed(), c.getGreen(), c.getBlue(), 255);
      }
      pixels[i] = row;
    }

    this.pixels = pixels;
    this.max = 255;
  }

  /**
   * takes in a file location and generates image data from file.
   *
   * @param filename file location
   */
  public EditorImageImpl(String filename) {
    readPPM(filename);
  }

  /**
   * takes in pixel data and max color value and generates image.
   *
   * @param pixels pixel data of image
   * @param max    max color value for each pixel
   */
  public EditorImageImpl(PixelRGB[][] pixels, int max) {
    height = pixels.length;
    if (pixels[0] == null) {
      width = 0;
    } else {
      width = pixels[0].length;
    }
    this.max = max;
    this.pixels = pixels;
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  private void readPPM(String filename) throws IllegalArgumentException {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with");
    }
    width = sc.nextInt();
    height = sc.nextInt();

    max = sc.nextInt();
    if (max <= 0) {
      throw new IllegalArgumentException("Max value not greater than 0");
    }

    pixels = new PixelRGBImpl[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        pixels[i][j] = new PixelRGBImpl(r, g, b, max);
      }
    }
  }

  /**
   * Gets the width of image.
   *
   * @return width as int
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of image.
   *
   * @return height as int
   */
  @Override
  public int getHeight() {
    return height;
  }

  /**
   * Gets the max value of colors in image.
   *
   * @return max value as int
   */
  @Override
  public int getMax() {
    return max;
  }

  /**
   * Returns the image data.
   *
   * @return 2D array of pixels
   */
  @Override
  public PixelRGB[][] getData() {
    return pixels;
  }

  @Override
  public BufferedImage toBufferedImage() {
    BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),
            BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        PixelRGB pixel = this.pixels[i][j];
        Color c = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());

        image.setRGB(j, i, c.getRGB());
      }
    }
    return image;
  }


  /**
   * Returns the image in same format as text file (minus any comments).
   *
   * @return Object as string
   */
  @Override
  public String toString() {
    StringBuilder returnStr = new StringBuilder("P3\n" + width + " " + height + "\n" + max + "\n");

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        returnStr.append(pixels[i][j].getRed() + "\n");
        returnStr.append(pixels[i][j].getGreen() + "\n");
        returnStr.append(pixels[i][j].getBlue() + "\n");
      }
    }

    return returnStr.toString();
  }
}
