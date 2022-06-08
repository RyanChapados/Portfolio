package controller.command;

import model.assignmentfive.PhotoEditorModelEnhanced;

/**
 * An extension on the PhotoControllerCommand that implements the ability to use the enhanced.
 * commands
 */
public interface EnhancedCommand extends PhotoControllerCommand {

  /**
   * Runs a single method from the model on the given object, then informs the user that the
   * method has run correctly.
   * @param m The model to run the method on
   * @throws IllegalArgumentException If the inputs given to the model are invalid
   */
  void run(PhotoEditorModelEnhanced m) throws IllegalArgumentException;
}
