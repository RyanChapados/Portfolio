package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.command.BrightenCommand;
import controller.command.ColorTransformCommand;
import controller.command.FilterCommand;
import controller.command.FlipCommand;
import controller.command.GreyscaleCommand;
import controller.command.LoadCommand;
import controller.command.PhotoControllerCommand;
import controller.command.SaveCommand;
import model.FlipType;
import model.Greyscale;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * A Controller for photo editor. This controller takes the user's input, and runs a function based
 * on that input or, if the input does not correlate to a function, tells the user that their input
 * was faulty. Will stop taking inputs once the user quits.
 */
public class PhotoEditorControllerImpl implements PhotoEditorController {
  private final Readable input;
  private final Map<String, Function<Scanner, PhotoControllerCommand>> knownCommands;
  private final PhotoEditorModel model;
  private final PhotoEditorView view;

  /**
   * Takes in a Readable, a model, and a view, and makes a controller object. It also creates a
   * hashmap of all the commands available to the user.
   *
   * @param input The input for all the commands the controller gets
   * @param model The model that will be acted upon by the controller
   * @param view  The destination for all messages sent to the user
   */
  public PhotoEditorControllerImpl(Readable input, PhotoEditorModel model, PhotoEditorView view)
          throws IllegalArgumentException {
    if (input == null) {
      throw new IllegalArgumentException("Input cannot be null");
    } else if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    } else if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }


    this.model = model;
    this.view = view;
    this.input = input;

    knownCommands = new HashMap<>();
    developCommands();
  }



  /**
   * Adds all the known commands to knownCommands.
   */
  private void developCommands() {
    knownCommands.put("brighten", s -> new BrightenCommand(s.next(),
            s.next(), s.next(), this.view));
    knownCommands.put("red-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Red, this.view));
    knownCommands.put("green-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Green, this.view));
    knownCommands.put("blue-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Blue, this.view));
    knownCommands.put("value-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Value, this.view));
    knownCommands.put("intensity-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Intensity, this.view));
    knownCommands.put("luma-component",
            s -> new GreyscaleCommand(s.next(), s.next(), Greyscale.Luma, this.view));
    knownCommands.put("horizontal-flip",
            s -> new FlipCommand(s.next(), s.next(), FlipType.Horizontal, this.view));
    knownCommands.put("vertical-flip",
            s -> new FlipCommand(s.next(), s.next(), FlipType.Vertical, this.view));
    knownCommands.put("load", s -> new LoadCommand(s.next(), s.next(), this.view));
    knownCommands.put("save", s -> new SaveCommand(s.next(), s.next(), this.view));
    double[][] sepia = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
    knownCommands.put("sepia",
            s -> new ColorTransformCommand(s.next(), s.next(), sepia, this.view));
    double[][] greyscale = {{0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}};
    knownCommands.put("greyscale",
            s -> new ColorTransformCommand(s.next(), s.next(), greyscale, this.view));
    double[][] blur = {
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};
    knownCommands.put("blur",
            s -> new FilterCommand(s.next(), s.next(), blur, this.view));
    double[][] sharpen = {{-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}};
    knownCommands.put("sharpen",
            s -> new FilterCommand(s.next(), s.next(), sharpen, this.view));
  }

  @Override
  public void runController() throws IllegalStateException {
    Scanner s = new Scanner(this.input);
    while (s.hasNext()) {
      String in = s.next();
      if (in.equalsIgnoreCase("quit")) {
        break;
      }
      Function<Scanner, PhotoControllerCommand> c =
              knownCommands.getOrDefault(in, null);
      if (c == null) {
        renderMessage("That is not a valid command");
      } else {
        try {
          PhotoControllerCommand command = c.apply(s);
          command.run(this.model);
        } catch (Exception e) {
          renderMessage("Those inputs do not work for the " + in + " command");
        }
      }
    }

    try {
      this.view.renderMessage("Thank you for using the photo editor!");
    } catch (Exception e) {
      throw new IllegalStateException("Error transmitting to view");
    }
  }

  /**
   * Renders a given message to the controller.
   *
   * @param s The message to render.
   * @throws IllegalStateException If transmission to the view fails.
   */
  private void renderMessage(String s) throws IllegalStateException {
    try {
      this.view.renderMessage(s);
      this.view.renderMessage("\n");
    } catch (Exception e) {
      throw new IllegalStateException("Error transmitting to view");
    }
  }
}