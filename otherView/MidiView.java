package cs3500.music.otherView;

/**
 * Generalized view to have methods to be implemented by all MIDI Views.
 * Done as a just-in-case.
 */
public interface MidiView extends View {

  void togglePlay();

  boolean isPlayingBool();

  void play();

  void pause();

  void changeVolume(int newVolume);

  MidiView getView();

  void changeTempo(int newtempo);


}
