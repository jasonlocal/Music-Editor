package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteModel;

import javax.sound.midi.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;

/**
 * View that plays Music via MIDI
 */
public class MidiViewImpl implements View {
  private Synthesizer synth;
  private Receiver receiver;
  private MusicEditorModel melody;
  private FileWriter logFile;

  public MidiViewImpl(MusicEditorModel model) {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
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

  public void playNote() throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 64);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 64);
    this.receiver.send(start, -1);
    this.receiver.send(stop, this.synth.getMicrosecondPosition() + 200000);
    this.receiver.close(); // Only call this once you're done playing all notes
  }

  @Override
  public void render() {
    System.out.println("Hogging the one and only main thread to play some " +
        "music!");

    long startTime = millisToSeconds(System.currentTimeMillis());
    long finishTime = startTime + melody.getMaxFinishTime() + 1;
    long current = 0;
    MidiChannel[] channels = synth.getChannels();

    //Loop for the duration of the song
    while (current < finishTime - startTime) {

      current = (millisToSeconds(System.currentTimeMillis()) - startTime);

      //Loop through each note, check to see if the current matches either the
      //start time of the note or the finish time. If it does, start playing
      //the note on the given instrument channel or stop playing the note.
      for (NoteModel n : melody.getNotes()) {
        if (n.getStartTime() == current && !n.isPlaying(melody.
            getCurrentBeat())) {
          channels[n.getInstrument()].noteOn(n.getPitch(), n.getVolume());
          try {
            logFile.write("cs3500.music.Note with pitch " + n.getPitch() +
                " began playing at " + current +
                    " at volume " + n.getVolume() + ".\n");
            logFile.write(System.getProperty("line.separator"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        if (n.getFinishTime() == current && n.isPlaying(melody.
            getCurrentBeat())) {
          channels[n.getInstrument()].noteOff(n.getPitch());
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
    }

    //Close the logfile and close the synthesizer.
    try {
      logFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    synth.close();
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
    return millis / 100;
  }

  public void addMouseListener(MouseListener mouseListener) {

  }

  public void addKeyListener(KeyListener k) {

  }

}





