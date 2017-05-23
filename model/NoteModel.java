package cs3500.music.model;

/**
 * Interface for Note representations
 */
public interface NoteModel {

  /**
   * chagne the pitch and time of the note upon moving
   *
   * @param newPitch int of the new Pitch
   * @param newStart int value of the new start time
   */
  void moveNote(int newPitch, int newStart);

  /**
   * check if the note is playing at the given time
   *
   * @param beat the beat, or time, to be checked
   * @return boolean value of the playing status of the note at the given beat
   */
  boolean isPlaying(int beat);

  /**
   * get the finish time of the note
   *
   * @return int value of the finish time
   */
  int getFinishTime();

  /**
   * get the start time of the note
   *
   * @return int value of the start time
   */
  int getStartTime();

  /**
   * get the pitch the note
   *
   * @return int value of the pitch of the note
   */
  int getPitch();


  /**
   * get the instrument  of the note
   *
   * @return int value of the instrument
   */
  int getInstrument();


  /**
   * get the volume of the note
   *
   * @return int value of the volume
   */
  int getVolume();

  /**
   * check if two notes are the same
   *
   * @param noteModel the note to be compared with
   * @return boolean value of the equality
   */
  boolean equal(NoteModel noteModel);

  void changeVolume(int volume);
}
