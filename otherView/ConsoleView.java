package cs3500.music.otherView;

/**
 * Holds all methods needed by all implementations of ConsoleView
 */
public interface ConsoleView extends View {
  /**
   * Returns some text-based log of the given view
   */
  String getViewLog();
}
