package view;

import java.io.IOException;

/**
 * A view class to display messages to the user.
 */
public class PhotoEditorViewImpl implements PhotoEditorView {
  private Appendable destination;

  /**
   * A constructor for AbstractSolitaireView. It can be used to view the state as a string.
   * @throws IllegalArgumentException If the model or destination is null.
   */
  public PhotoEditorViewImpl(Appendable destination)
          throws IllegalArgumentException {
    if (destination == null) {
      throw new IllegalArgumentException("Destination must not be null");
    }

    this.destination = destination;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    try {
      this.destination.append(message);
    } catch (Exception e) {
      throw new IOException("Transmission of message to data destination failed");
    }
  }
}
