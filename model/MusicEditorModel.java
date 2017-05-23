package cs3500.music.model;

import cs3500.music.view.ViewObserver;

import java.util.ArrayList;

/**
 * Model interface for MusicEditors.
 */
public interface MusicEditorModel {
  /**
   * Adds note(s) to the cs3500.music.MusicEditorImpl. Also handles aligning
   * the time intervals and putting the note at the right spot.
   *
   * @param notes a variadic list of all cs3500.music.cs3500.music.Note objects
   *              to pass in.
   *              The user will specify inside of the cs3500.music.cs3500.
   *              music.Note object
   *              -> the type of the note (ie: D3)
   *              -> the start and finish time
   * @throws MusicEditorImpl.InvalidNoteException this exception
   * is thrown if the user tries to add a note that overlaps with an existing
   * note of the same type.
   */
  void addNotes(NoteModel... notes)
          throws MusicEditorImpl.InvalidNoteException;

//  /**
//   * Removes a note from the cs3500.music.MusicEditorImpl. The way this will
//   * work is that the user will enter a note type and a start and finish
//   * interval, and the melody will delete all notes that fall between those
//   * intervals and resize those that overlap.
//   * Consider the example:
//   * 1 2 3 4 5 6 7 8 9 10
//   * C# - -   - - -     - -
//   * If removeNoteInterval(Notes.CSharp, 4, 9) was called, the new melody
//   * would be:
//   * 1 2 3 4 5 6 7 8 9 10
//   * C# - -               -
//   *
//   * @param pitch  Type of note to remove
//   * @param start  Start time to remove the notes. This index is included in
//   *               the delete
//   * @param finish Finish time to remove notes. This index is included in the
//   *               delete
//   */
//  void removeNoteInterval(int pitch, int start, int finish);

  /**
   * Registers a cs3500.music.view observer with the cs3500.music.MusicEditorImpl.
   * This way, views can register themselves with the cs3500.music.model to
   * receiver updates.
   *
   * @param obs The cs3500.music.view to register. All views will eventually
   *            implement cs3500.music.ViewObserver.
   */
  void registerViewObserver(ViewObserver obs);

  /**
   * Removes a cs3500.music.view observer with the cs3500.music.MusicEditorImpl.
   * This could occur if a cs3500.music.view is no longer necessary, for
   * example.
   *
   * @param obs The cs3500.music.view observer to remove.
   */
  void removeViewObserver(ViewObserver obs);

  /**
   * Notifies all of the cs3500.music.view observers by calling update()
   * on each one.
   */
  void notifyViews();

  /**
   * Searches the note list looking for the note with the maximum finish time.
   * @return the max finish time
   */
  int getMaxFinishTime();

  /**
   * Get the note
   *
   * @return the complex notes making up the melody.
   */
  ArrayList<NoteModel> getNotes();

  /**
   * get the minimum pitch of the music melody in integer value
   *
   * @return the int of minimum pitch
   */
  int getMinPitch();

  /**
   * get the maximum pitch of the music melody in integer value
   *
   * @return the int of maximum pitch
   */
  int getMaxPitch();

  /**
   * get the current time point of the music playing
   *
   * @return the int of current beat
   */
  int getCurrentBeat();


  static String pitchToNote(int pitch) throws RuntimeException {
    String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A",
        "A#", "B"};
    int[] octave = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8};

    if (pitch < 0 || pitch > 127)
    {
      throw new RuntimeException("Bad pitch");
    }
    else
    {
      return notes[pitch % 12].concat(Integer.toString(octave[(pitch-(pitch %
          12))/12]));
    }
  }

  /**
   * puase the music playing
   */
  void pauseMusic();

  /**
   * start the music play
   */
  void startMusicPlay();

  /**
   * check if the music is playing now
   *
   * @return boolean value of the playing status of the music
   */
  boolean getPlayingStatus();

  /**
   * get the tempo of the music
   *
   * @return int value of the tempo
   */
  int getTempo();

  /**
   * get all the notes playing at the given time and pitch
   *
   * @param beat the int of the time
   * @param pitch the int of the beat
   * @return an arrayList of the notes at the given beat and pitch
   */
  ArrayList<NoteModel> getNotesByBeatAndPitch(int beat, int pitch);

  /**
   * remove note from the model
   *
   * @param noteModel the note to be removed
   */
  void removeNote(NoteModel noteModel);

  /**
   * set current beat to the given number
   *
   * @param setBeat the beat int to be set
   */

  void setCurrentBeat(int setBeat);

  /**
   * get all the notes playing at the given time
   *
   * @param beat the int of the time
   * @return an arrayList of the notes at the given beat
   */
  ArrayList<NoteModel> getNotesPlayingAt(int beat);

  /**
   * check if the new NoteModel is overlapping with any existing notes
   *
   * @param n the NoteModel to be added
   * @return true if there is
   */
  boolean checkIfThereIsNoteOverlap(NoteModel n);

  /**
   * get all the notes playing with the given pitch
   *
   * @param pitch the int of the pitch
   * @return an arrayList of the notes at the given pitch
   */
  ArrayList<NoteModel> getNotesByPitch(int pitch);

//  /**
//   * adapted method to get the Playables at the given point
//   *
//   * @param beat the given beat in int
//   * @return the collection of the IPlayables at the given beat
//   */
//  Collection<IPlayable> getPlayablesAtBeat(int beat);
//
//  /**
//   * get the IPlayable, aka Note, of the highest Pitch
//   *
//   * @return the IPlayable with the highest pitch
//   */
//  IPlayable getHighestPlayable();
//  /**
//   * get the IPlayable, aka Note, of the lowest Pitch
//   *
//   * @return the IPlayable with the highest pitch
//   */
//  IPlayable getLowestPlayable();
//
//
//  int getLastBeat();
//  boolean containsBeat(int beat);
//  MusicEditorModel getComposition();
//  boolean isEmpty();
//  void removePlayable(IPlayable playable);
//  void addPlayable(IPlayable playable) throws MusicEditorImpl.InvalidNoteException;
//  void changeVolume(int volume);
//  Collection<IPlayable> getAllPlayables();
}


