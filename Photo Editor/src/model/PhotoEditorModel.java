package model;

/**
 * An interface representing all the actions a PhotoEditor can perform.
 */
public interface PhotoEditorModel {

  /**
   * Gets the width of this model's image.
   *
   * @return The width of the image
   * @throws IllegalArgumentException if image does not exist
   */
  int getImageWidth(String imageName) throws IllegalArgumentException;

  /**
   * Gets the height of this model's image.
   *
   * @return The height of the image
   * @throws IllegalArgumentException if image does not exist
   */
  int getImageHeight(String imageName) throws IllegalArgumentException;

  /**
   * Brightens the given image by the given increment, then save it to the given destination.
   * If the increment is negative, will darken the image.
   *
   * @param imageName The name of the image to brighten
   * @param destName  The location to store the image to
   * @param inc       The increment to brighten the image by
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  void brighten(String imageName, String destName, int inc) throws IllegalArgumentException;

  /**
   * Greyscales the given image based on the given Greyscale, then saves it to the given
   * destination.
   *
   * @param imageName The name of the image to greyscale
   * @param destName  The location to store the image to
   * @param type      The type of greyscale to perform
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  void greyscale(String imageName, String destName, Greyscale type)
          throws IllegalArgumentException;

  /**
   * Flips the given image based on the given FlipType, then saves it to the given destination.
   *
   * @param imageName The name of the image to flip
   * @param destName  The location to store the image to
   * @param type      The type of flip to perform
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  void flip(String imageName, String destName, FlipType type) throws IllegalArgumentException;

  /**
   * Stores the given image at the given dest.
   * @param dest The place to store the image
   * @param im The image to store
   * @throws IllegalArgumentException If the given inputs are not valid
   */
  void putImage(String dest, EditorImage im) throws IllegalArgumentException;

  /**
   * Returns the image with the given name from the model. If image is not found, throws an
   * exception
   * @param imageName The name of the image to be returned
   * @return A PPMImage from the given imageName
   * @throws IllegalArgumentException If no image with the given name exists
   */
  EditorImage getImage(String imageName) throws IllegalArgumentException;
}