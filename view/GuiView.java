package cs3500.music.view;


import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Interface for Gui specifics such as methods for dealing with keyboard and
 * mouse
 */
public interface GuiView extends View {
  void addMouseListener(MouseListener mouseListener);
  void addKeyListener(KeyListener k);

  int getClickedBeat(int xCoordinate);
  int getClickedPitch(int yCoordinate);
}
