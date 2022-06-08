package controller.command;

import model.PhotoEditorModel;
import view.PhotoEditorGUI;
import view.PhotoEditorView;

/**
 * An object to run the method brighten on a given model.
 */
public class BrightenCommand extends AbstractControllerCommand {
  private int inc;

  /**
   * Sets the increment which will be used in the brighten method.
   *
   * @param inc       The increment to be used
   * @param imageName The name of the image to be brightened
   * @param destName  The destination of the brightened image
   * @throws NumberFormatException If the inc string does not contain an int
   */
  public BrightenCommand(String imageName, String destName, String inc, PhotoEditorView view)
          throws NumberFormatException {
    super(imageName, destName, view);
    if (inc == null) {
      throw new IllegalArgumentException("inc cannot be null");
    }
    this.view = (PhotoEditorGUI) view;
    this.inc = Integer.parseInt(inc);
  }

  /**
   * Runs the brighten method on the given model.
   *
   * @param m The model to run brighten on
   */
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    m.brighten(this.imageName, this.destName, this.inc);
    renderMessage(this.imageName + " has been brightened by "
              + this.inc + " and saved as " + this.destName + "\n");
  }
}
