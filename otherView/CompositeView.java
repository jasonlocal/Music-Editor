package cs3500.music.otherView;

import javax.sound.midi.MetaEventListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Created by duane on 4/4/2016.
 */
public class CompositeView implements GuiView {

  private final GuiView guiView;
  private final MidiView midiView;

  public CompositeView(ViewModel model) {
    guiView = new GuiViewImpl(model);
    midiView = new MidiViewImpl(model);
  }


  @Override
  public void initialize() {
    guiView.initialize();
    midiView.initialize();
    guiView.setCurrentBeat(midiView.getCurrentBeat());
  }

  @Override
  public void resetFocus() {

  }

  @Override
  public void reinitialize() {
    guiView.setCurrentBeat(midiView.getCurrentBeat());
    guiView.reinitialize();
    midiView.reinitialize();
  }


  /**
   * Toggle play/stop in the midi view.
   */
  public void togglePlay() {midiView.togglePlay();}

  /**
   * Control the position of the scroll pane in the gui view.
   * @param horiz, true or false for controlling the horizontal or vertical view.
   * @param scrollUnit number of units to scroll
   */
  public void setScrollPos(boolean horiz, int scrollUnit){
    guiView.setScrollPos(horiz,scrollUnit);
  }


  /**
   * Retrieving object of view type interface.
   * @return view
   */
  public View getView() {
    return this;
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    guiView.addKeyListener(listener);
  }

  @Override
  public void addActionListener(ActionListener actionListener) {
    guiView.addActionListener(actionListener);
  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {
    guiView.addMouseListener(mouseListener);
  }

  @Override
  public void addMetaListener(MetaEventListener metaListener) {
    midiView.addMetaListener(metaListener);
  }

  @Override
  public void setCurrentBeat(int beat) {
    midiView.setCurrentBeat(beat);
    guiView.setCurrentBeat(beat);
  }

  @Override
  public int getCurrentBeat() {
    return midiView.getCurrentBeat();
  }

  public GuiView getGuiView() {
    return guiView;
  }

  public MidiView getMidiView() {
    return midiView;
  }
}
