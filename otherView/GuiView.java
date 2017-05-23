package cs3500.music.otherView;

/**
 * Created by duane on 3/16/2016.
 */
public interface GuiView extends View {

  /**
   * Initialize the GuiView to make it visable.
   */
  void initialize();

  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  void resetFocus();

  /**
   * Scroll the ScrollPane to in a direction by a certain number of units
   *
   * @param horiz
   * @param scrollUnits
   */
  void setScrollPos(boolean horiz, int scrollUnits);

}
