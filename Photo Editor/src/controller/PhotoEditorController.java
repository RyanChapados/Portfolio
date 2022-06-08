package controller;

/**
 * An interface for a photo editor controller. Shows all the operations the controller must be able
 * to take.
 */
public interface PhotoEditorController {

  /**
   * This method runs the photo editor, by taking inputs from the user and passing them to the
   * model.
   * @throws IllegalStateException If controller fails to read input or transmit output
   */
  void runController() throws IllegalStateException;
}
