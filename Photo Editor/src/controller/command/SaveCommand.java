package controller.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.EditorImage;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * An object to run the method save on a given model.
 */
public class SaveCommand extends AbstractControllerCommand {
  /**
   * A constructor for the save command.
   *
   * @param path      The place the image will be saved to
   * @param imageName The image to be saved
   */
  public SaveCommand(String path, String imageName, PhotoEditorView view) {
    super(path, imageName, view);
  }

  @Override
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    if (this.imageName.substring(imageName.length() - 3, imageName.length()).equals("ppm")) {
      savePPM(m);
    } else {
      saveOther(m);
    }

    renderMessage(this.destName + " has been successfully saved as " + this.imageName
            + "\n");
  }

  private void savePPM(PhotoEditorModel m) {
    File fileToSave = new File(this.imageName);

    try {
      FileWriter fileWriter = new FileWriter(fileToSave, false);
      fileWriter.write(m.getImage(this.destName).toString());
      fileWriter.close();
    } catch (Exception e) {
      throw new IllegalArgumentException("Saving failed: " + e);
    }
  }

  private void saveOther(PhotoEditorModel m) {
    EditorImage im = m.getImage(this.destName);
    BufferedImage image = im.toBufferedImage();

    File file = new File(this.imageName);
    try {
      ImageIO.write(image, this.imageName.substring(imageName.length() - 3, imageName.length()),
              file);
    } catch (IOException e) {
      throw new IllegalArgumentException("Saving failed: " + e);
    }
  }
}
