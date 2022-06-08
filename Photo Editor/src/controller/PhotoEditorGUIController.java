package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.command.ColorTransformCommand;
import controller.command.FilterCommand;
import controller.command.FlipCommand;
import controller.command.GUIBrightenCommand;
import controller.command.GreyscaleCommand;
import controller.command.LoadCommand;
import controller.command.PhotoControllerCommand;
import controller.command.SaveCommand;
import model.FlipType;
import model.Greyscale;
import model.PhotoEditorModel;
import view.PhotoEditorGUI;

public class PhotoEditorGUIController implements ActionListener, PhotoEditorController {
  private String boxEntry;
  private String command;
  private String path;

  private PhotoEditorGUI view;
  private final Map<String, Function<String, PhotoControllerCommand>> knownCommands;
  private final PhotoEditorModel model;

  /**
   * Takes in a Readable, a model, and a view, and makes a controller object. It also creates a
   * hashmap of all the commands available to the user.
   *
   * @param model The model that will be acted upon by the controller
   * @param view  The GUI that the user will interact with
   */
  public PhotoEditorGUIController(PhotoEditorModel model, PhotoEditorGUI view) throws
          IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    } else if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }

    this.model = model;
    this.command = "";
    this.boxEntry = "brighten";
    this.path = "";
    this.view = view;
    this.knownCommands = new HashMap<>();
    developCommands();
  }

  private void developCommands() {
    knownCommands.put("brighten", s -> new GUIBrightenCommand("image","image", this.view));
    knownCommands.put("red-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Red, this.view));
    knownCommands.put("green-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Green, this.view));
    knownCommands.put("blue-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Blue, this.view));
    knownCommands.put("value-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Value, this.view));
    knownCommands.put("intensity-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Intensity, this.view));
    knownCommands.put("luma-component",
            s -> new GreyscaleCommand("image", "image", Greyscale.Luma, this.view));
    knownCommands.put("horizontal-flip",
            s -> new FlipCommand("image", "image", FlipType.Horizontal, this.view));
    knownCommands.put("vertical-flip",
            s -> new FlipCommand("image", "image", FlipType.Vertical, this.view));
    knownCommands.put("load", s -> new LoadCommand(s, "image", this.view));
    knownCommands.put("save", s -> new SaveCommand(s, "image", this.view));
    double[][] sepia = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
    knownCommands.put("sepia",
            s -> new ColorTransformCommand("image", "image", sepia, this.view));
    double[][] greyscale = {{0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}};
    knownCommands.put("greyscale",
            s -> new ColorTransformCommand("image", "image", greyscale, this.view));
    double[][] blur = {{0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};
    knownCommands.put("blur",
            s -> new FilterCommand("image", "image", blur, this.view));
    double[][] sharpen = {{-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}};
    knownCommands.put("sharpen",
            s -> new FilterCommand("image", "image", sharpen, this.view));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String input = e.getActionCommand();

    //Sets this.command to the command in the combo box
    if (input.equals("set command")) {
      if (e.getSource() instanceof JComboBox) {
        JComboBox<String> box = (JComboBox<String>) e.getSource();
        String s = (String) box.getSelectedItem();
        this.boxEntry = s;
        this.command = s;
      }

    //Executes this.command if there is an image loaded and this.command is not load or save
    } else if (input.equals("execute")) {
      //Ensures there is an image loaded
      try {
        this.model.getImage("image");
        this.command = this.boxEntry;
        runController();
      } catch (Exception error) {
        renderMessage("Error//No image loaded");
      }

    //Loads an image using the filechooser
    } else if (input.equals("load")) {
      final JFileChooser fchooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
              "JPG, PPM, and PNG Images", "jpg", "png", "ppm");
      fchooser.setFileFilter(filter);
      int retvalue = fchooser.showOpenDialog((JFrame) this.view);
      if (retvalue == JFileChooser.APPROVE_OPTION) {
        File f = fchooser.getSelectedFile();
        this.path = f.getAbsolutePath();
        this.command = "load";
        runController();
      }
    }

    //Saves an image using the file chooser
    else if (input.equals("save")) {
      final JFileChooser fchooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
              "JPG, PPM, and PNG Images", "jpg", "png", "ppm");
      fchooser.setFileFilter(filter);
      int retvalue = fchooser.showSaveDialog((JFrame) this.view);
      if (retvalue == JFileChooser.APPROVE_OPTION) {
        File f = fchooser.getSelectedFile();
        this.path = f.getAbsolutePath();

        //Ensures the extension is valid
        if (path.endsWith("ppm") || path.endsWith("jpg") || path.endsWith("png")) {
          this.command = "save";
          runController();
        }
        else {
          renderMessage("Error//Cannot save files of that type");
        }
      }
    }
  }

  /**
   * Takes inputs from the action performed command, and runs them.
   *
   * @throws IllegalStateException If controller fails to read input or transmit output
   */
  public void runController() throws IllegalStateException {
    Function<String, PhotoControllerCommand> f =
            knownCommands.getOrDefault(this.command, null);

    //Ensures the command is not null
    if (f == null) {
      renderMessage("Error//Unknown Command");
    } else {

      //Sends the error message to the user if the program throws an error
      try {
        PhotoControllerCommand c = f.apply(this.path);
        c.run(this.model);
        this.view.setPhoto(model.getImage("image").toBufferedImage());
      } catch (IllegalArgumentException e) {
        renderMessage("Error//" + e.getMessage());
      }
    }
  }

  private void renderMessage(String s) {
    try {
      this.view.renderMessage(s);
    } catch (IOException e) {
      throw new IllegalStateException("Error transmitting to view");
    }
  }
}
