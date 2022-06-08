package model;

/**
 * Ways to flip an image: horizontal and vertical.
 */
public enum FlipType {
  Horizontal("horizontal"), Vertical("vertical");

  private final String descriptor;

  /**
   * returns descriptor (kind of flip).
   * @return descriptor as string.
   */
  public String toString() {
    return descriptor;
  }

  FlipType(String d) {
    this.descriptor = d;
  }
}
