package model.assignmentfive;

import model.PhotoEditorModel;

/**
 * An interface containing upgrades to the PhotoEditorModel.
 */
public interface PhotoEditorModelEnhanced extends PhotoEditorModel {
  /**
   * Transforms the colors in the image based on the given array, then saves it to the given.
   * destination
   *
   * @param imageName The name of the image to be colorTransformed
   * @param destName  The key to map the image to
   * @param arr       The array to multiply the rgb values of each pixel by
   * @throws IllegalArgumentException If the imageName does not correspond to an image, or any value
   *                                  in the array is null
   */
  void colorTransformation(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException;

  /**
   * Filters the image based on the given array, then saves it to the given.
   * destination
   *
   * @param imageName The name of the image to be colorTransformed
   * @param destName  The key to map the image to
   * @param arr       The kernel to filter by
   * @throws IllegalArgumentException If the imageName does not correspond to an image, or any value
   *                                  in the array is null
   */
  void filter(String imageName, String destName, double[][] arr)
          throws IllegalArgumentException;
}
