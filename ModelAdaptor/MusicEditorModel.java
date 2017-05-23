package cs3500.music.ModelAdaptor;

import cs3500.music.model.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface of model for the other group's view
 */
public interface MusicEditorModel extends cs3500.music.model.MusicEditorModel {
  /**
   * get all the notes playing with the given pitch
   *
   * @param pitch the int of the pitch
   * @return an arrayList of the notes at the given pitch
   */
  ArrayList<NoteModel> getNotesByPitch(int pitch);

  /**
   * adapted method to get the Playables at the given point
   *
   * @param beat the given beat in int
   * @return the collection of the IPlayables at the given beat
   */
  Collection<IPlayable> getPlayablesAtBeat(int beat);

  /**
   * get the IPlayable, aka Note, of the highest Pitch
   *
   * @return the IPlayable with the highest pitch
   */
  IPlayable getHighestPlayable();
  /**
   * get the IPlayable, aka Note, of the lowest Pitch
   *
   * @return the IPlayable with the highest pitch
   */
  IPlayable getLowestPlayable();


  int getLastBeat();
  boolean containsBeat(int beat);
  cs3500.music.model.MusicEditorModel getComposition();
  boolean isEmpty();
  void removePlayable(IPlayable playable);
  void addPlayable(IPlayable playable) throws cs3500.music.model.MusicEditorImpl.InvalidNoteException;
  void changeVolume(int volume);
  Collection<IPlayable> getAllPlayables();

  int getTempo();
}
