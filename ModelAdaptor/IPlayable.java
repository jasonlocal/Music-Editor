package cs3500.music.ModelAdaptor;

/**
 * Interface of Note used by the other group's view
 */
public interface IPlayable {
  int getPitch();
  int getStartBeat();
  int getVolume();
  int getDuration();
  int getInstrument();
}
