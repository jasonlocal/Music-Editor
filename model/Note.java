package cs3500.music.model;

import cs3500.music.ModelAdaptor.IPlayable;

/**
 * class of note with complicated details.
 */
public class Note implements NoteModel {
  private int start;
  private int end;
  private final int instrument;
  private int pitch;
  private int volume;
  public Note(int start, int end, int instrument, int pitch, int volume)
  {
    this.start = start;
    this.end = end;
    this.instrument = instrument;
    this.pitch = pitch;
    this.volume = volume;
  }

  public Note(IPlayable iPlayable) {
    this.start = iPlayable.getStartBeat();
    this.end = iPlayable.getStartBeat() + iPlayable.getDuration();
    this.instrument = iPlayable.getInstrument();
    this.pitch = iPlayable.getPitch();
    this.volume = iPlayable.getVolume();

  }

  public void changeVolume(int volume) {
    this.volume = volume;
  }

  public int getStartTime() {
    return start;
  }

  /**
   * get the finish time of the note
   *
   * @return int value of the finish time
   */
  @Override
  public int getFinishTime() {
    return this.end;
  }

  public int getPitch() {
    return pitch;
  }

  public int getInstrument() {
    return instrument;
  }

  public int getVolume() {
    return volume;
  }

  /**
   * check if the note is playing at the given time
   *
   * @param beat the beat, or time, to be checked
   * @return boolean value of the playing status of the note at the given beat
   */
  public boolean isPlaying(int beat)
  {
    return beat <= end && beat >= start;
  }

  /**
   * chagne the pitch and time of the note upon moving
   *
   * @param newPitch int of the new Pitch
   * @param newStart int value of the new start time
   */
  @Override
  public void moveNote(int newPitch, int newStart) {
    int duration = this.end - this.start;
    this.pitch = newPitch;
    this.start = newStart;
    this.end = newStart + duration;
  }

  /**
   * check if two notes are the same
   *
   * @param noteModel the note to be compared with
   * @return boolean value of the equality
   */
  @Override
  public boolean equal(NoteModel noteModel) {
    return this.start == noteModel.getStartTime() &&
        this.end == noteModel.getFinishTime() &&
        this.instrument == noteModel.getInstrument() &&
        this.pitch == noteModel.getPitch() &&
        this.volume == noteModel.getVolume();
  }

  /**
   * Converts some pitch number to it's equivalent String Independent from Note
   *
   * @param pitch some int [0, 128)
   * @return String of given pitch
   */
  public static String pitchToString(int pitch) {
    int abspitch;
    if (pitch < 0) {
      abspitch = 12 + pitch;
    } else {
      abspitch = pitch;
    }
    if ((abspitch % 12) == 0) {
      return "C" + pitch/12;
    } else if ((abspitch % 12) == 1) {
      return "C#" + pitch/12;
    } else if ((abspitch % 12) == 2) {
      return "D" + pitch/12;
    } else if ((abspitch % 12) == 3) {
      return "D#" + pitch/12;
    } else if ((abspitch % 12) == 4) {
      return "E" + pitch/12;
    } else if ((abspitch % 12) == 5) {
      return "F" + pitch/12;
    } else if ((abspitch % 12) == 6) {
      return "F#" + pitch/12;
    } else if ((abspitch % 12) == 7) {
      return "G" + pitch/12;
    } else if ((abspitch % 12) == 8) {
      return "G#" + pitch/12;
    } else if ((abspitch % 12) == 9) {
      return "A" + pitch/12;
    } else if ((abspitch % 12) == 10) {
      return "A#" + pitch/12;
    } else if ((abspitch % 12) == 11){
      return "B" + pitch/12;
    } else {
      throw new IllegalStateException("There has been some error with the note number");
    }
  }
}
