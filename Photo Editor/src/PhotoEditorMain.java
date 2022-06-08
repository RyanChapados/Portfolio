import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.PhotoEditorController;
import controller.PhotoEditorControllerImpl;
import controller.PhotoEditorGUIController;
import model.PhotoEditorModel;
import model.PhotoEditorModelImpl;
import view.PhotoEditorGUI;
import view.PhotoEditorGUIView;
import view.PhotoEditorView;
import view.PhotoEditorViewImpl;

/**
 * Main class for photo editor. Load/save files and brighten/darken, flip, or turn to greyscale!
 * Inputs seperated by spaces or newlines.
 */
public class PhotoEditorMain {

  /**
   * main method.
   *
   * @param args command line args (none needed)
   */
  public static void main(String[] args) {
    PhotoEditorModel model = new PhotoEditorModelImpl();
    PhotoEditorGUI gui = new PhotoEditorGUIView();
    PhotoEditorController c = new PhotoEditorGUIController(model, gui);
    gui.setListener((ActionListener) c);
    gui.showGUI(true);
    try {
      gui.renderMessage("hi");
    } catch (IOException e) {

    }

  }
}
