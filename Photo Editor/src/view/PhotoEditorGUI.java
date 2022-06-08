package view;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * An extension of the View interface that allows for displaying a GUI.
 */
public interface PhotoEditorGUI extends PhotoEditorView {

  /**
   * Hides or shows this GUI depending on the value of b
   * @param b If true shows the GUI if false hides it
   */
  void showGUI(boolean b);

  /**
   * Sets this ActionListener to l
   * @param l
   */
  void setListener(ActionListener l);

  /**
   * Sets the current image to the given photo.
   * @param i The image to display
   */
  void setPhoto(Image i);

  /**
   * Gets input from the user in a box displayed by the GUI.
   * @param message The message to display to the user
   * @return The input from the user
   */
  String getInput(String message);
}
