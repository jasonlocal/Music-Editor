package cs3500.music.otherView;

import cs3500.music.ModelAdaptor.IPlayable;

import javax.sound.midi.MetaEventListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collection;


/**
 * Creates a CompositionView Object Done to keep View and Model seperate
 */
public class ConsoleViewImpl implements ConsoleView {


  private final ViewModel model;
  private StringBuilder stringBuilder = new StringBuilder();
  /**
   * Constructor for the Composition View Done explicitly to keep these methods seperate from
   * Composition
   *
   */
  public ConsoleViewImpl(ViewModel model){
    this.model = model;
  }

  /**
   * Displays the state of the grid for the user.
   *
   */
  @Override
  public void initialize() {
    StringBuilder leftFormat = new StringBuilder();
    leftFormat.append("%").append(Integer.toString
            (model.getLastBeat()).length()).append("s");
    stringBuilder.append(String.format(leftFormat.toString(), " "));
    stringBuilder.append("\n");
    for (int i = 0; i < model.getLastBeat(); i++) {
      Object[] listofpitches = new Object[128];
      initialize(model, listofpitches);
      stringBuilder.append(String.format(leftFormat.toString(), i));

      if (model.containsBeat(i)) {
        Collection<IPlayable> playableCollection = model.getPlayablesAtBeat(i);
        for (IPlayable note : playableCollection) {
          listofpitches[note.getPitch()] = (note.getStartBeat() == i);
        }
        for (Object o : Arrays.copyOfRange
                (listofpitches, model.getLowestPlayable().getPitch(),
                        model.getHighestPlayable().getPitch() + 1)) {
          if (o.equals(true)) {
            stringBuilder.append(" X ");
          }
          if (o.equals(false)) {
            stringBuilder.append(" | ");
          }
          if (o.equals("")) {
            stringBuilder.append("   ");
          }
        }
      }
      stringBuilder.append("\n");
    }
    System.out.print(stringBuilder.toString());
  }

  @Override
  public void setCurrentBeat(int beat) {
    throw new IllegalStateException("Not needed");
  }

  @Override
  public int getCurrentBeat() {
    return 0;
  }

  /**
   * Initializes an array with 128 "" Done to "place" notes in their positions, so they can be
   * called and written accurately
   *
   * @param array Array to be initialized
   */
  private void initialize(ViewModel composition, Object... array) {
    for (int x = 0; x < composition.getHighestPlayable().getPitch()+1; x++) {
      array[x] = "";
    }
  }

  @Override
  public String getViewLog() {
    return stringBuilder.toString();
  }

  public View getView() {
    return this;
  }


  // NOT NEEDED BUT REQUIRED TO BE IMPLEMENTED
  @Override
  public void reinitialize() {
    throw new IllegalStateException("Not needed for this view");
  }

  @Override
  public void addKeyListener(KeyListener listener) {

  }

  @Override
  public void addActionListener(ActionListener listener) {

  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {

  }


  @Override
  public void addMetaListener(MetaEventListener metaListener) {

  }
}