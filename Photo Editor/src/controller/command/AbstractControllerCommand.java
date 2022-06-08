package controller.command;

import java.io.IOException;

import model.assignmentfive.PhotoEditorModelEnhanced;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * An abstract class for Controller Command. Allows abstraction of contructors and fields.
 */
public abstract class AbstractControllerCommand implements EnhancedCommand {
  protected String imageName;
  protected String destName;
  protected PhotoEditorView view;

  /**
   * An abstract constructor for controller command.
   * @param imageName The name of the image that the command will be performed upon
   * @param destName The destination of the image
   */
  public AbstractControllerCommand(String imageName, String destName, PhotoEditorView view) {
    if (imageName == null || destName == null || view == null) {
      throw new IllegalArgumentException("Inputs cannot be null");
    }
    this.imageName = imageName;
    this.destName = destName;
    this.view = view;
  }

  @Override
  public void run(PhotoEditorModelEnhanced m) {
    run((PhotoEditorModel) m);
  }

  /**
   * Renders a message to the view.
   * @param s The message to render
   */
  protected void renderMessage(String s) {
    try {
      this.view.renderMessage(s);
    }
    catch (IOException e) {
      throw new IllegalStateException("Error transmitting to view");
    }
  }
}
