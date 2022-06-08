import java.awt.event.ActionListener;

import controller.PhotoEditorController;

import controller.PhotoEditorGUIController;
import model.PhotoEditorModel;
import model.PhotoEditorModelImpl;
import view.PhotoEditorGUI;
import view.PhotoEditorGUIView;


public class GUIMain {

  public static void main(String[] args) {
    PhotoEditorModel model = new PhotoEditorModelImpl();
    PhotoEditorGUI gui = new PhotoEditorGUIView();
    PhotoEditorController c = new PhotoEditorGUIController(model, gui);
    gui.setListener((ActionListener) c);
    gui.showGUI(true);
  }
}
