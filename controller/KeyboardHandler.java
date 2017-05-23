package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Class to Handle Key Events.
 */
public class KeyboardHandler implements KeyListener {
  private final Map<Integer, Runnable> onPress;
  private final Map<Integer, Runnable> onRelease;
  private final Map<Integer, Runnable> onTyped;

  public KeyboardHandler(Map<Integer, Runnable> onPress,
                         Map<Integer, Runnable> onRelease,
                         Map<Integer, Runnable> onTyped) {
    if (onPress != null && onRelease != null && onTyped != null) {
      this.onPress = onPress;
      this.onRelease = onRelease;
      this.onTyped = onTyped;
    } else {
      throw new IllegalArgumentException("Arguments must be non-null");
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (onTyped.get(e.getKeyCode()) != null) {
      onTyped.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (onPress.get(e.getKeyCode()) != null) {
      onPress.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (onRelease.get(e.getKeyCode()) != null) {
      onRelease.get(e.getKeyCode()).run();
    }
  }
}
