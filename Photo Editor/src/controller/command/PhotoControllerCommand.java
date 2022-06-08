package controller.command;

import model.PhotoEditorModel;

/**
 * This interface represents a command that the photo editor can take in from the user.
 */
public interface PhotoControllerCommand {

  /**
   * Runs a single method from the model on the given object, then informs the user that the
   * method has run correctly.
   * @param m The model to run the method on
   * @throws IllegalArgumentException If the inputs given to the model are invalid
   */
  void run(PhotoEditorModel m) throws IllegalArgumentException;
}
