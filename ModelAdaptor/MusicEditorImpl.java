package cs3500.music.ModelAdaptor;

import cs3500.music.model.Note;
import cs3500.music.model.NoteModel;
import cs3500.music.util.CompositionBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  Adaptor class for the others group's view access to model
 */
public class MusicEditorImpl extends cs3500.music.model.MusicEditorImpl
    implements MusicEditorModel {

  public MusicEditorImpl() {
    super();
  }

  /**
   * adapted method to get the Playables at the given point
   *
   * @param beat the given beat in int
   * @return the collection of the IPlayables at the given beat
   */
  @Override
  public Collection<IPlayable> getPlayablesAtBeat(int beat) {
    ArrayList<NoteModel> temp = this.getNotesPlayingAt(beat);
    ArrayList<IPlayable> convertedToOtherNote = new ArrayList<>(temp.size());
    for (int i=0; i < temp.size(); i++) {
      convertedToOtherNote.add(i, new OtherNote((Note)temp.get(i)));
    }
    return convertedToOtherNote;
  }

  /**
   * get the IPlayable, aka Note, of the highest Pitch
   *
   * @return the IPlayable with the highest pitch
   */
  @Override
  public IPlayable getHighestPlayable() {
    int temp =  this.getMaxPitch();
    for (NoteModel noteModel : this.notes) {
      if (noteModel.getPitch() == temp) {
        return new OtherNote((Note)noteModel);
      }
    }
    throw new IllegalStateException("something weird went wrong");
  }

  /**
   * get the IPlayable, aka Note, of the lowest Pitch
   *
   * @return the IPlayable with the highest pitch
   */
  @Override
  public IPlayable getLowestPlayable() {
    int temp =  this.getMinPitch();
    for (NoteModel noteModel : this.notes) {
      if (noteModel.getPitch() == temp) {
        return new OtherNote((Note)noteModel);
      }
    }
    throw new IllegalStateException("something weird went wrong");
  }




  @Override
  public int getLastBeat() {
    return this.getMaxFinishTime();
  }

  @Override
  public boolean containsBeat(int beat) {
    for(NoteModel note : this.notes) {
      if (note.isPlaying(beat)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public cs3500.music.model.MusicEditorModel getComposition() {
    return this;
  }

  @Override
  public boolean isEmpty() {
    return this.notes.isEmpty();
  }

  @Override
  public void removePlayable(IPlayable playable) {
    NoteModel temp = new Note(playable);
    this.removeNote(temp);
  }

  @Override
  public void addPlayable(IPlayable playable) throws InvalidNoteException {
    NoteModel temp = new Note(playable);
    this.addNotes(temp);
  }

  @Override
  public Collection<IPlayable> getAllPlayables() {
    List<IPlayable> temp = new ArrayList<>();
    for (NoteModel noteModel : this.notes){
      temp.add(new OtherNote((Note)noteModel));
    }
    return temp;
  }

  @Override
  public int getTempo() {
    return this.tempo;
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
}
