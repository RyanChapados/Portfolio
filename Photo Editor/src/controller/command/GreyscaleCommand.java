package controller.command;

import model.Greyscale;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * A class to perform the Greyscale command. This class runs the greyscale command on a given model.
 */
public class GreyscaleCommand extends AbstractControllerCommand {
  private Greyscale type;

  /**
   * A constructor for greyscale command. Sets the type of greyscale.
   *
   * @param imageName The name of the image that the greyscale will be performed upon
   * @param destName  The destination of the modified image
   * @param type The kind of greyscale that will be performed
   */
  public GreyscaleCommand(String imageName, String destName, Greyscale type,
                          PhotoEditorView view) {
    super(imageName, destName, view);
    if (type == null) {
      throw new IllegalArgumentException("type cannot be null");
    }
    this.type = type;
  }

  @Override
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    m.greyscale(this.imageName, this.destName, this.type);
    renderMessage(this.imageName + " has been greyscaled based on "
              + this.type.toString() + " and saved as " + this.destName + "\n");
  }
}