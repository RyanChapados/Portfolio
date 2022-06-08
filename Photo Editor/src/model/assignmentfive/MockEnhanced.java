package model.assignmentfive;

import model.MockModel;
import model.EditorImage;

/**
 * mock model for enhanced methods.
 */
public class MockEnhanced extends MockModel implements PhotoEditorModelEnhanced {

  /**
   * Creates a Mock model with the given log.
   *
   * @param log The log to used to create the mock
   */
  public MockEnhanced(StringBuilder log) {
    super(log);
  }


  @Override
  public EditorImage getImage(String imageName) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void colorTransformation(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + "destName: " + destName + "\n");
  }

  @Override
  public void filter(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException {
    this.log.append("imageName: " + imageName + "destName: " + destName + "\n");

  }
}
