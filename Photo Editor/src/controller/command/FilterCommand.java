package controller.command;

import model.assignmentfive.PhotoEditorModelEnhanced;
import model.PhotoEditorModel;
import view.PhotoEditorView;

/**
 * An object to run the method filter on a given model.
 */
public class FilterCommand extends AbstractControllerCommand {
  private double[][] arr;

  /**
   * Sets the method of filtering.
   *
   * @param imageName The name of the image to be filtered
   * @param destName  The destination of the filtered image
   * @param arr       the array to use
   * @throws NumberFormatException If the array is null or not odd or not a square
   */
  public FilterCommand(String imageName, String destName, double[][] arr,
                       PhotoEditorView view) throws IllegalArgumentException {
    super(imageName, destName, view);

    if (arr == null) {
      throw new IllegalArgumentException("Arr cannot be null");
    }

    this.arr = arr;
  }

  /**
   * Will fail if the given model is not enhanced.
   *
   * @param m The model to run the method on
   * @throws IllegalArgumentException If the model is not enhanced
   */
  public void run(PhotoEditorModel m) throws IllegalArgumentException {
    run((PhotoEditorModelEnhanced) m);
    renderMessage(this.imageName + " has been filtered and saved as "
            + this.destName + "\n");
  }

  @Override
  public void run(PhotoEditorModelEnhanced m) throws IllegalArgumentException {
    m.filter(this.imageName, this.destName, this.arr);
  }
}
