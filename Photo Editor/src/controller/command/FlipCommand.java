package controller.command;

import model.FlipType;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * An object to run the method flip on a given model.
 */
public class FlipCommand extends AbstractControllerCommand {
  private FlipType type;

  /**
   * Sets the type of flip which will be used.
   * @param imageName The image to be flipped
   * @param destName The destination of the flipped image
   * @param type The type of flip to perform
   */
  public FlipCommand(String imageName, String destName, FlipType type, PhotoEditorView view) {
    super(imageName,destName,view);
    if (type == null) {
      throw new IllegalArgumentException("type cannot be null");
    }
    this.type = type;
  }

  /**
   * Flips the image in the model based on the type of flip, then stores it to the destination.
   * @param m The model to run the method on
   * @throws IllegalArgumentException If the arguments are invalid
   */
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    m.flip(this.imageName, this.destName, this.type);
    renderMessage(this.imageName + " has been flipped "
              + this.type.toString() + "ly and saved as " + this.destName + "\n");
  }
}
