package model;

/**
 * Represents a pixel (3 integers for each color). Minimum and max color values deteremined by
 * implementation.
 */
public interface PixelRGB {

  /**
   * Sets red pixel value.
   * @param red value to set to
   */
  public void setRed(int red);

  /**
   * Sets green pixel value.
   * @param green value to set to
   */
  public void setGreen(int green);

  /**
   * Sets blue pixel value.
   * @param blue value to set to
   */
  public void setBlue(int blue);

  /**
   * Returns red pixel value.
   * @return red value as integer
   */
  public int getRed();

  /**
   * Returns green pixel value.
   * @return green value as integer
   */
  public int getGreen();

  /**
   * Returns blue pixel value.
   * @return blue value as integer
   */
  public int getBlue();
}
