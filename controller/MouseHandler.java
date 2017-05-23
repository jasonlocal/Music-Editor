package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.function.Consumer;

/**
 *  class for MouseListener implementation
 */
public class MouseHandler implements MouseListener {
  private final Map<Integer, Consumer<MouseEvent>> onClick;
  private final Map<Integer, Consumer<MouseEvent>> onPress;
  private final Map<Integer, Consumer<MouseEvent>> onRelease;

  public MouseHandler(Map<Integer, Consumer<MouseEvent>> onClick,
                      Map<Integer, Consumer<MouseEvent>> onPress,
                      Map<Integer, Consumer<MouseEvent>> onRelease) {
    if (onClick == null || onPress == null || onRelease == null) {
      throw new IllegalArgumentException("Arguments must be non-null");
    }
    this.onClick = onClick;
    this.onPress = onPress;
    this.onRelease = onRelease;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (this.onClick.get(e.getButton()) != null) {
      this.onClick.get(e.getButton()).accept(e);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (this.onPress.get(e.getButton()) != null) {
      this.onPress.get(e.getButton()).accept(e);
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (this.onRelease.get(e.getButton()) != null) {
      this.onRelease.get(e.getButton()).accept(e);
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}
