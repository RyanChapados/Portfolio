package model;

/**
 * types of greyscale.
 * Either by color (red, green, blue)
 * Value (highest value)
 * Intensity (average value)
 * Luma (0.2126r+0.7152g+0.0722b)
 */
public enum Greyscale {
  Red("red"), Green("green"), Blue("blue"), Value("value"),
    Intensity("intensity"), Luma("luma");

  private final String descriptor;

  /**
   * returns descriptor (kind of greyscale).
   * @return descriptor as string.
   */
  public String toString() {
    return descriptor;
  }

  Greyscale(String d) {
    this.descriptor = d;
  }
}
