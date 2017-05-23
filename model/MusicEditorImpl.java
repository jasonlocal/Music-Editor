package cs3500.music.model;

import cs3500.music.util.CompositionBuilder;
import cs3500.music.view.ViewObserver;

import java.util.ArrayList;

/**
 * class for the entire melody.
 */
public class MusicEditorImpl implements MusicEditorModel {

  //This next arraylist is for MIDI's purposes.
  protected ArrayList<NoteModel> notes;
  protected int maxPitch = 0;
  protected int minPitch = Integer.MAX_VALUE;
  protected int tempo = 0;
  protected ArrayList<ViewObserver> viewObservers;
  protected int currentBeat;
  protected boolean playingStatus;

  public void changeVolume(int volume) {
    for (NoteModel noteModel : this.notes) {
      noteModel.changeVolume(volume);
    }
  }

  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  /**
   * Custom exception for InvalidNotes.
   */
  public class InvalidNoteException extends Exception {
    private String message;

    public InvalidNoteException(String message) {
      super(message);
      this.message = message;
    }
  }

  public MusicEditorImpl() {;
    this.viewObservers = new ArrayList<>();
    this.notes = new ArrayList<>();
    this.tempo = 100;
    this.currentBeat = 0;
    this.playingStatus = false;
  }

  @Override
  public void addNotes(NoteModel... notes) throws InvalidNoteException {
    for (NoteModel n : notes) {
      //Basic sanity checks
      if (n.getStartTime() < 0 || n.getStartTime() >= n.getFinishTime()) {
        throw new InvalidNoteException("Invalid time interval.");
      }
      //If melody.addNote returns false, the note was not added.
      if (this.checkIfThereIsNoteOverlap(n)) {
        //Which means we throw an exception.
        throw new InvalidNoteException("There is a note overlap for the note "+
            "type " + pitchToNote(n.getPitch()) + " between the intervals " +
            n.getStartTime() + "<->" + n.getFinishTime() + ".");
      }
      this.notes.add(n);
    }
  }

  @Override
  public void registerViewObserver(ViewObserver obs) {
    viewObservers.add(obs);
    obs.update(this);
  }

  @Override
  public void removeViewObserver(ViewObserver obs) {
    viewObservers.remove(obs);
  }

  @Override
  public void notifyViews() {
    for (ViewObserver v : viewObservers) {
      v.update(this);
    }
  }

  public static String pitchToNote(int pitch) throws RuntimeException {
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
   * check if the new NoteModel is overlapping with any existing notes
   *
   * @param n the NoteModel to be added
   * @return true if there is
   */
  @Override
  public boolean checkIfThereIsNoteOverlap(NoteModel n) {
    for (NoteModel existingNotes : this.notes) {
      if (existingNotes.getPitch() == n.getPitch()) {
        if (!(existingNotes.getFinishTime() <= n.getStartTime() ||
            existingNotes.getStartTime() >= n.getFinishTime())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get the note
   *
   * @return the complex notes making up the melody.
   */
  @Override
  public ArrayList<NoteModel> getNotes() {
    return this.notes;
  }

  /**
   * get all the notes playing at the given time and pitch
   *
   * @param beat  the int of the time
   * @param pitch the int of the beat
   * @return an arrayList of the notes at the given beat and pitch
   */
  @Override
  public ArrayList<NoteModel> getNotesByBeatAndPitch(int beat, int pitch) {
    ArrayList<NoteModel> temp = new ArrayList<>();
    for (NoteModel existingNotes : this.notes) {
      if(existingNotes.getPitch() == pitch && existingNotes.isPlaying(beat)) {
        temp.add(existingNotes);
      }
    }
    if (temp.isEmpty()) {
      throw new IllegalArgumentException("No Note found at the given beat and" +
          " pitch");
    }
    return temp;
  }

  /**
   * set current beat to the given number
   *
   * @param setBeat the beat int to be set
   */
  @Override
  public void setCurrentBeat(int setBeat) {
    this.currentBeat = setBeat;
  }

  /**
   * Searches the note list looking for the note with the maximum finish time.
   * @return the max finish time
   */
  public int getMaxFinishTime() {
    int max = 0;
    for (NoteModel n : notes) {
      if (n.getFinishTime()+1 > max) {
        max = n.getFinishTime()+1;
      }
    }

    return max;
  }

  /**
   * get the maximum pitch of the music melody in integer value
   *
   * @return the int of maximum pitch
   */
  public int getMaxPitch() {
    int temp = 0;
    for (NoteModel noteModel : this.notes) {
      if (noteModel.getPitch() > temp) {
        temp = noteModel.getPitch();
      }
    }
    return temp;
  }

  /**
   * get the minimum pitch of the music melody in integer value
   *
   * @return the int of minimum pitch
   */
  public int getMinPitch() {
    int temp = this.notes.get(0).getPitch();
    for (NoteModel noteModel : this.notes) {
      if (noteModel.getPitch() < temp) {
        temp = noteModel.getPitch();
      }
    }
    return temp;
  }

  /**
   * get the current time point of the music playing
   *
   * @return the int of current beat
   */
  @Override
  public int getCurrentBeat() {
    return this.currentBeat;
  }


  /**
   * puase the music playing
   */
  @Override
  public void pauseMusic() {
    this.playingStatus = false;
  }

  /**
   * start the music play
   */
  @Override
  public void startMusicPlay() {
    this.playingStatus = true;
  }

  /**
   * check if the music is playing now
   *
   * @return boolean value of the playing status of the music
   */
  @Override
  public boolean getPlayingStatus() {
    return this.playingStatus;
  }

  /**
   * get the tempo of the music
   *
   * @return int value of the tempo
   */
  @Override
  public int getTempo() {
    return this.tempo;
  }

  /**
   * get all the notes playing at the given time
   *
   * @param beat the int of the time
   * @return an arrayList of the notes at the given beat
   */
  @Override
  public ArrayList<NoteModel> getNotesPlayingAt(int beat) {
    ArrayList<NoteModel> temp = new ArrayList<>();
    for (NoteModel n : this.getNotes()) {
      if (n.isPlaying(beat)) {
        temp.add(n);
      }
    }
    return temp;
  }

  /**
   * remove note from the model
   *
   * @param noteModel the note to be removed
   */
  @Override
  public void removeNote(NoteModel noteModel) {
    boolean noteFound = false;
    for (int i = 0; i < this.notes.size(); i++) {
      if (this.notes.get(i).equal(noteModel)) {
        this.notes.remove(i);
        noteFound = true;
      }
    }
    if (!noteFound) {
      throw new IllegalArgumentException("No such Note found in the music");
    }
  }

  /**
   * get all the notes playing with the given pitch
   *
   * @param pitch@return an arrayList of the notes at the given pitch
   */
  @Override
  public ArrayList<NoteModel> getNotesByPitch(int pitch) {
    ArrayList<NoteModel> temp = new ArrayList<>();
    for (NoteModel n : this.getNotes()) {
      if (n.getPitch() == pitch) {
        temp.add(n);
      }
    }
    return temp;
  }

  public static final class Builder implements CompositionBuilder<
      MusicEditorModel> {
    private MusicEditorImpl model = new MusicEditorImpl();

    @Override
    public MusicEditorModel build() {
      return model;
    }

    @Override
    public CompositionBuilder<MusicEditorModel> setTempo(int tempo) {
      this.model.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<MusicEditorModel> addNote(
        int start, int end, int instrument, int pitch, int volume) {
      try {
        model.addNotes(new Note(start, end-1, instrument, pitch,
            volume));
      } catch (MusicEditorImpl.InvalidNoteException e) {
        e.printStackTrace();
      }
      return this;
    }
  }

//  /**
//   * adapted method to get the Playables at the given point
//   *
//   * @param beat the given beat in int
//   * @return the collection of the IPlayables at the given beat
//   */
//  @Override
//  public Collection<IPlayable> getPlayablesAtBeat(int beat) {
//    ArrayList<NoteModel> temp = this.getNotesPlayingAt(beat);
//    ArrayList<IPlayable> convertedToOtherNote = new ArrayList<>(temp.size());
//    for (int i=0; i < temp.size(); i++) {
//      convertedToOtherNote.add(i, new OtherNote((Note)temp.get(i)));
//    }
//    return convertedToOtherNote;
//  }
//
//  /**
//   * get the IPlayable, aka Note, of the highest Pitch
//   *
//   * @return the IPlayable with the highest pitch
//   */
//  @Override
//  public IPlayable getHighestPlayable() {
//    int temp =  this.getMaxPitch();
//    for (NoteModel noteModel : this.notes) {
//      if (noteModel.getPitch() == temp) {
//        return new OtherNote((Note)noteModel);
//      }
//    }
//    throw new IllegalStateException("something weird went wrong");
//  }
//
//  /**
//   * get the IPlayable, aka Note, of the lowest Pitch
//   *
//   * @return the IPlayable with the highest pitch
//   */
//  @Override
//  public IPlayable getLowestPlayable() {
//    int temp =  this.getMinPitch();
//    for (NoteModel noteModel : this.notes) {
//      if (noteModel.getPitch() == temp) {
//        return new OtherNote((Note)noteModel);
//      }
//    }
//    throw new IllegalStateException("something weird went wrong");
//  }
//
//
//
//
//  @Override
//  public int getLastBeat() {
//    return this.getMaxFinishTime();
//  }
//
//  @Override
//  public boolean containsBeat(int beat) {
//    for(NoteModel note : this.notes) {
//      if (note.isPlaying(beat)) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  @Override
//  public MusicEditorModel getComposition() {
//    return this;
//  }
//
//  @Override
//  public boolean isEmpty() {
//    return this.notes.isEmpty();
//  }
//
//  @Override
//  public void removePlayable(IPlayable playable) {
//    NoteModel temp = new Note(playable);
//    this.removeNote(temp);
//  }
//
//  @Override
//  public void addPlayable(IPlayable playable) throws InvalidNoteException {
//    NoteModel temp = new Note(playable);
//    this.addNotes(temp);
//  }
//
//  @Override
//  public Collection<IPlayable> getAllPlayables() {
//    List<IPlayable> temp = new ArrayList<>();
//    for (NoteModel noteModel : this.notes){
//      temp.add(new OtherNote((Note)noteModel));
//    }
//    return temp;
//  }
}
