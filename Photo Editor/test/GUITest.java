import org.junit.Test;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import controller.PhotoEditorController;
import controller.PhotoEditorGUIController;
import model.PhotoEditorModel;
import model.PhotoEditorModelImpl;
import view.PhotoEditorGUI;
import view.PhotoEditorGUIView;

public class GUITest {

  @Test
  public void test() throws IOException {
    PhotoEditorModel model = new PhotoEditorModelImpl();
    PhotoEditorGUI gui = new PhotoEditorGUIView();
    PhotoEditorController c = new PhotoEditorGUIController(model, gui);
    gui.setListener((ActionListener) c);
    gui.showGUI(true);

    gui.renderMessage("hi");
  }
}
