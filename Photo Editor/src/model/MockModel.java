package model;

import model.assignmentfive.PhotoEditorModelEnhanced;

/**
 * A mock class of PhotoEditorModel. This class simply appends the inputs given to it to the log,
 * so that they can be tested.
 */
public class MockModel implements PhotoEditorModelEnhanced {
  protected final StringBuilder log;

  /**
   * Creates a Mock model with the given log.
   * @param log The log to used to create the mock
   */
  public MockModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public int getImageWidth(String imageName) {
    return 0;
  }

  @Override
  public int getImageHeight(String imageName) {
    return 0;
  }

  @Override
  public void brighten(String imageName, String destName, int inc) throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + " destName: " + destName
            + " inc: " + inc + "\n");
  }

  @Override
  public void greyscale(String imageName, String destName, Greyscale type)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + " destName: " + destName
            + " FlipType: " + type.toString() + "\n");
  }

  @Override
  public void flip(String imageName, String destName, FlipType type)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + " destName: " + destName
            + " FlipType: " + type.toString() + "\n");
  }

  @Override
  public void putImage(String dest, EditorImage im) throws IllegalArgumentException {
    //Does nothing.
  }

  @Override
  public EditorImage getImage(String imageName) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void colorTransformation(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + " destName: " + destName);
  }

  @Override
  public void filter(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + " destName: " + destName);
  }
}
