package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteModel;

import java.util.ArrayList;

/**
 * Console view represent string format of midi
 */
public class ConsoleViewImpl implements View
{
  private MusicEditorModel melody;

  public ConsoleViewImpl(MusicEditorModel model)
  {
    model.registerViewObserver(this);
  }

  @Override
  public void render() {
    StringBuilder console = new StringBuilder("    ");

    //Top row:
    for (int noteType = melody.getMinPitch(); noteType <= melody.getMaxPitch();
         noteType++)
    {
      console.append("\t").append(MusicEditorModel.pitchToNote(
          noteType));
    }
    for (int time = 0; time < melody.getMaxFinishTime(); time++) {
      //Left colum with time. Apply conditions to make it occupy four digital
      // space
      if (time <= 9) {
        console.append("\n").append(time).append("   ");
      }
      else if (time <= 99) {
        console.append("\n").append(time).append("  ");
      }
      else if (time <= 999) {
        console.append("\n").append(time).append(" ");
      }
      else if (time <= 9999) {
        console.append("\n").append(time).append("");
      }
      else throw new IllegalStateException("Wow that is a long music");

      //Loop through each note type. Check if the note started at the
      //time. If so, append an 'x'. Otherwise, if the note overlaps with
      //the time, append an '|'. Otherwise, do nothing.
      for (int pitch = melody.getMinPitch(); pitch <= melody.getMaxPitch();
           pitch++) {
        console.append("\t");
        ArrayList<NoteModel> temp = this.melody.getNotesByBeatAndPitch(pitch,
            time);
        for (NoteModel noteModel : temp)
        {
          if (noteModel.getStartTime() == time) {
            console.append("X");
          } else {
            console.append("|");
          }
        }
      }
    }

    System.out.println(console.toString());
  }

  public String getConsole() {
    StringBuilder console = new StringBuilder();

    //Top row:
    for (int noteType = melody.getMinPitch(); noteType <= melody.getMaxPitch();
         noteType++) {
      console.append("\t").append(MusicEditorModel.pitchToNote(
          noteType));
    }
    for (int time = 0; time < melody.getMaxFinishTime(); time++) {
      //Left colum with time:
      console.append("\n").append(time);

      //Loop through each note type. Check if the note started at the
      //time. If so, append an 'x'. Otherwise, if the note overlaps with
      //the time, append an '|'. Otherwise, do nothing.
      for (int pitch = melody.getMinPitch(); pitch <= melody.getMaxPitch();
           pitch++) {
        console.append("\t");
        ArrayList<NoteModel> temp = this.melody.getNotesByBeatAndPitch(pitch,
            time);
        for (NoteModel noteModel : temp)
        {
          if (noteModel.getStartTime() == time) {
            console.append("X");
          } else {
            console.append("|");
          }
        }
      }

    }

    return console.toString();
  }

  @Override
  public void update(MusicEditorModel melody) {
    this.melody = melody;
  }
}
