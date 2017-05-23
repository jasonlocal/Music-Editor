package cs3500.music.tests;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteModel;
import cs3500.music.view.View;

import javax.sound.midi.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Mock MIDI for debugging purposes.
 */
public class MockDebugMidi implements View
{
  private Synthesizer synth;
  private Receiver receiver;
  private MusicEditorModel melody;
  private FileWriter logFile;

  public MockDebugMidi(MusicEditorModel model){
    model.registerViewObserver(this);
    try {
      logFile = new FileWriter("audiblelog.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   *  <li>{@link MidiSystem#getSynthesizer()}</li>
   *  <li>{@link Synthesizer}
   *    <ul>
   *      <li>{@link Synthesizer#open()}</li>
   *      <li>{@link Synthesizer#getReceiver()}</li>
   *      <li>{@link Synthesizer#getChannels()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link Receiver}
   *    <ul>
   *      <li>{@link Receiver#send(MidiMessage, long)}</li>
   *      <li>{@link Receiver#close()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link MidiMessage}</li>
   *  <li>{@link ShortMessage}</li>
   *  <li>{@link MidiChannel}
   *    <ul>
   *      <li>{@link MidiChannel#getProgram()}</li>
   *      <li>{@link MidiChannel#programChange(int)}</li>
   *    </ul>
   *  </li>
   * </ul>
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   *   https://en.wikipedia.org/wiki/General_MIDI
   *   </a>
   */

  @Override
  public void render()
  {
    long startTime = millisToSeconds(System.currentTimeMillis());
    long finishTime = startTime + melody.getMaxFinishTime() + 1;
    long current = 0;

    //Loop for the duration of the song
    //This should probably run on a separate thread since it
    //loops for the entire duration of the song.
    while (current < finishTime - startTime)
    {
      for (NoteModel n : melody.getNotes())
      {
        if (n.getStartTime() == current && !n.isPlaying(melody.getCurrentBeat()))
        {
          //Log the note being played, this is for testing purposes.
          try {
            logFile.write("cs3500.music.Note with pitch " + n.getPitch() +
                " began playing at " + current +
                    " at volume " + n.getVolume() + ".\n");
            logFile.write(System.getProperty("line.separator"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        if (n.getFinishTime() == current && n.isPlaying(melody.getCurrentBeat()))
        {
          //Log the end of the note.
          try {
            logFile.write("cs3500.music.Note with pitch " + n.getPitch() +
                " stopped playing at " + current +
                    ".\n");
            logFile.write(System.getProperty("line.separator"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      current++;
    }

    //Close the logfile
    try {
      logFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String testMidi(int second)
  {

    for (NoteModel n : melody.getNotes())
    {
      if (n.getStartTime() == second)
      {
        //Log the note being played, this is for testing purposes.
        return ("cs3500.music.Note with pitch " + n.getPitch() + " began playing at " + second +
                " at volume " + n.getVolume());
      }

      if (n.getFinishTime() == second)
      {
        return ("cs3500.music.Note with pitch " + n.getPitch() + " stopped playing at " + second);
      }
    }

    return "Nothing";
  }

  @Override
  public void update(MusicEditorModel melody) {
    this.melody = melody;
  }

  /**
   * Converts milliseconds to seconds
   * @param millis milliseconds
   * @return seconds
   */
  private long millisToSeconds(long millis)
  {
    return millis / 1000;
  }
}
