package controller.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.EditorImage;
import model.EditorImageImpl;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * An object to run the method load on a given model.
 */
public class LoadCommand extends AbstractControllerCommand {
  /**
   * A constructor for the load command.
   *
   * @param path The place the image will be loaded from
   * @param imageName  The place the image will be saved in the model
   */
  public LoadCommand(String path, String imageName, PhotoEditorView view) {
    super(path, imageName, view);
  }

  @Override
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    try {
      if (this.imageName.endsWith("ppm")) {
        loadPPM(m);
      } else {
        loadOther(m);
      }
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Error reading file: " + e);
    }

    renderMessage("Image has been successfully loaded and saved as " + this.destName
            + "\n");
  }

  private void loadPPM(PhotoEditorModel m) {
    EditorImage im = new EditorImageImpl(this.imageName);
    m.putImage(destName, im);
  }

  private void loadOther(PhotoEditorModel m) throws IllegalArgumentException {
    File file = new File(imageName);
    BufferedImage image;

    try {
      image = ImageIO.read(file);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("Error reading file");
    }

    EditorImage i = new EditorImageImpl(image);

    m.putImage(destName, i);
  }
}