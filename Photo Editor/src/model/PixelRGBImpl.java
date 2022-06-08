package model;

/**
 * Represents a pixel (3 integers for each color). Minimum color is 0. Max color is determined by
 * instructor.
 */
public class PixelRGBImpl implements PixelRGB {
  private int max;
  private int red;
  private int green;
  private int blue;

  /**
   * Sets each color value to given value along with max. If out of bounds, set to bound.
   *
   * @param r red
   * @param g green
   * @param b blue
   * @param m max color value. Must be greater than 0
   */
  public PixelRGBImpl(int r, int g, int b, int m) throws IllegalArgumentException {
    if (m <= 0) {
      throw new IllegalArgumentException("Max not greater than 0");
    }
    max = m;
    setRed(r);
    setGreen(g);
    setBlue(b);
  }

  /**
   * Sets red pixel value. If out of bounds, set to bound.
   *
   * @param red value to set to
   */
  @Override
  public void setRed(int red) {
    if (red < 0) {
      this.red = 0;
    } else if (red > max) {
      this.red = max;
    } else {
      this.red = red;
    }
  }

  /**
   * Sets green pixel value. If out of bounds, set to bound.
   *
   * @param green value to set to
   */
  @Override
  public void setGreen(int green) {
    if (green < 0) {
      this.green = 0;
    } else if (green > max) {
      this.green = max;
    } else {
      this.green = green;
    }
  }

  /**
   * Sets blue pixel value. If out of bounds, set to bound.
   *
   * @param blue value to set to
   */
  @Override
  public void setBlue(int blue) {
    if (blue < 0) {
      this.blue = 0;
    } else if (blue > max) {
      this.blue = max;
    } else {
      this.blue = blue;
    }
  }

  /**
   * Returns red pixel value.
   *
   * @return red value as integer
   */
  @Override
  public int getRed() {
    return red;
  }

  /**
   * Returns green pixel value.
   *
   * @return green value as integer
   */
  @Override
  public int getGreen() {
    return green;
  }

  /**
   * Returns blue pixel value.
   *
   * @return blue value as integer
   */
  @Override
  public int getBlue() {
    return blue;
  }
}