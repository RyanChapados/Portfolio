package model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Interface for PPMImage. Contains methods to get width, height, and data within a PPMImage.
 */
public interface EditorImage {
  /**
   * Gets the width of image.
   * @return width as int
   */
  int getWidth();

  /**
   * Gets the height of image.
   * @return height as int
   */
  int getHeight();

  /**
   * Gets the max value of colors in image.
   * @return max value as int
   */
  int getMax();

  /**
   * Returns the image data.
   * @return 2D array of pixels
   */
  PixelRGB[][] getData();

  /**
   * Converts this EditorImage into a BufferedImage.
   * @return A BufferedImage of this EditorImage
   */
  BufferedImage toBufferedImage();
}
