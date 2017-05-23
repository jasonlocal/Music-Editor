package cs3500.music.ModelAdaptor;

import cs3500.music.model.Note;

/**
 * Note implementation used by the other group
 */
public class OtherNote extends Playable {
  private int pitch, startBeat, volume, duration, instrument;

  public OtherNote(Note note) {
    this.pitch = note.getPitch();
    this.startBeat = note.getStartTime();
    this.volume = note.getVolume();
    this.duration = note.getFinishTime() - note.getStartTime();
    this.instrument = note.getInstrument();
  }

  @Override
  public int getInstrument() {
    return this.instrument;
  }

  @Override
  public int getPitch() {
    return this.pitch;
  }

  @Override
  public int getStartBeat() {
    return this.startBeat;
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  @Override
  public int getDuration() {
    return this.duration;
  }
}
