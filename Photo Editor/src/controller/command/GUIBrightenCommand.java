package controller.command;

import model.PhotoEditorModel;
import view.PhotoEditorGUI;
import view.PhotoEditorView;

/**
 * A brighten command that assumes the user is using a PhotoEditorGUI
 */
public class GUIBrightenCommand extends AbstractControllerCommand {
  private int inc;

  /**
   * A constructor for brighten command that assumes the view is a GUI.
   *
   * @param imageName The name of the image that the command will be performed upon
   * @param destName  The destination of the image
   * @param view      Assumed to be a GUI view
   */
  public GUIBrightenCommand(String imageName, String destName, PhotoEditorView view) {
    super(imageName, destName, view);
    this.inc = getInc();
  }

  @Override
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    m.brighten(this.imageName, this.destName, this.inc);
  }

  private int getInc() {
    PhotoEditorGUI tempView = (PhotoEditorGUI) this.view;
    String s = tempView.getInput("Enter the amount to brighten the image by: ");
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Increment must be a number");
    }
  }

}
