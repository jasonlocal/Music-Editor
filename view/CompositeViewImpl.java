package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Class implementation of the Composited View
 */
public class CompositeViewImpl extends JFrame implements GuiView {
  private MusicEditorModel m;
  private GuiViewImpl view;
  private MidiViewImpl midi;

  public CompositeViewImpl(MusicEditorModel m) {
    this.m = m;
    this.view = new GuiViewImpl(m);
    this.midi = new MidiViewImpl(m);
  }

  @Override
  public void addKeyListener(KeyListener k) {
    view.addKeyListener(k);
    midi.addKeyListener(k);
  }

  /**
   * Renders the current state of the cs3500.music.MusicEditorImpl.
   * cs3500.music.Note, this doesn't just assume graphical rendering. Views
   * will also call and implement this function for actually playing audio as
   * well.
   */
  @Override
  public void render() {
    this.view.render();
    this.midi.render();
  }

  /**
   * Adds a mouseListener to the view
   *
   * @param ml represents the mouseListener to be added
   */
  public void addMouseListener(MouseListener ml) {
  }

  /**
   * This is invoked by the Subject, and the View is responsible
   * for responding to this update.
   *
   * @param melody melody data to update cs3500.music.view with
   */
  @Override
  public void update(MusicEditorModel melody) {
    this.m = melody;
    this.view = new GuiViewImpl(melody);
    this.midi = new MidiViewImpl(melody);
    this.render();
  }

  /**
   * restart the playing by setting the current beat back to zero
   */
  public void restartPlay() {
    this.m.setCurrentBeat(0);
  }

  @Override
  public int getClickedBeat(int xCoordinate) {
    return this.view.getClickedBeat(xCoordinate);
  }

  @Override
  public int getClickedPitch(int yCoordinate) {
    return this.view.getClickedPitch(yCoordinate);
  }
}
