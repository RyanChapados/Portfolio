package view;

import java.io.IOException;

/**
 * This interface represents a view for photo editor.
 */
public interface PhotoEditorView {

  /**
   * Render the given message to a data destination.
   * @param message The message to be transmitted
   */
  void renderMessage(String message) throws IOException;
}
