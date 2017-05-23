package cs3500.music.otherView;

import cs3500.music.ModelAdaptor.IPlayable;

import javax.sound.midi.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.nio.ByteBuffer;


/**
 * Implementation of the MIDI View
 */
public class MidiViewImpl implements MidiView {


  private Sequencer sequencer;

  private Sequence sequence;

  private Track track;

  private int volume = 100;

  private boolean playingBool;

  private final ViewModel model;

  private int tempo;

  // The Offset Constant of the Note.
  // Fun: Change the value to change the key.
  //      12 is an exact octave above, adjust to suit
  private final int noteOffsetConstant = 12;

  // Main Constructor
  public MidiViewImpl(ViewModel model) {
    this.model = model;
    this.tempo = model.getTempo() * 4;
    Sequencer tempseqr = null;
    Sequence tempseq = null;
    try {
      tempseqr = MidiSystem.getSequencer();
      try {
        tempseq = new Sequence(Sequence.PPQ, 4);
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
      if (tempseqr == null) {
        System.out.println("Cannot get a sequencer");
        System.exit(0);
      }
      tempseqr.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    sequencer = tempseqr;
    sequence = tempseq;

    playingBool = false;
  }

  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   * <li>{@link MidiSystem#getSynthesizer()}</li>
   * <li>{@link Synthesizer}
   * <ul>
   * <li>{@link Synthesizer#open()}</li>
   * <li>{@link Synthesizer#getReceiver()}</li>
   * <li>{@link Synthesizer#getChannels()}</li>
   * </ul>
   * </li>
   * <li>{@link Receiver}
   * <ul>
   * <li>{@link Receiver#send(MidiMessage, long)}</li>
   * <li>{@link Receiver#close()}</li>
   * </ul>
   * </li>
   * <li>{@link MidiMessage}</li>
   * <li>{@link ShortMessage}</li>
   * <li>{@link MidiChannel}
   * <ul>
   * <li>{@link MidiChannel#getProgram()}</li>
   * <li>{@link MidiChannel#programChange(int)}</li>
   * </ul>
   * </li>
   * </ul>
   *
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   * https://en.wikipedia.org/wiki/General_MIDI
   * </a>
   */

  /**
   * Sends a given note to the midi receiver
   *
   * @param playable some playable item
   */
  private MidiEvent playNote(IPlayable playable) throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, playable.getInstrument(),
            playable.getPitch()
                    + noteOffsetConstant, volume);
    return new MidiEvent(start, playable.getStartBeat());
  }

  /**
   * Stops a note from being sent to the receiver
   *
   * @param playable some playable note.
   */
  private MidiEvent stopNote(IPlayable playable) throws InvalidMidiDataException {
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, playable.getInstrument(),
            playable.getPitch()
                    + noteOffsetConstant, volume);
    return new MidiEvent(stop, playable.getStartBeat() + playable.getDuration());
  }

  @Override
  public void initialize() {
    try {
      createMIDITrack();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void reinitialize() {
    initialize();
  }

  /**
   * Creates a MIDI track to be played, based on the model
   */
  private void createMIDITrack() throws InvalidMidiDataException {
    sequence.deleteTrack(track);
    track = sequence.createTrack();
    for (int i = 0; i <= model.getLastBeat(); i++) {
      if (model.containsBeat(i)) {
        for (IPlayable p : model.getPlayablesAtBeat(i)) {
          if (i == p.getStartBeat()) {
            try {
              track.add(this.playNote(p));
              track.add(this.stopNote(p));
            } catch (InvalidMidiDataException e) {
              e.printStackTrace();
            }
          }
        }
      }
      for (int j = 0; j < model.getLastBeat(); j++) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(j).array();
        MidiEvent midiEvent = new MidiEvent(
                new MetaMessage(1, ByteBuffer.allocate(4).putInt(j).array(),
                        bytes.length), j);
        track.add(midiEvent);
      }
    }
  }

  /**
   * Feel free to use it, but we no longer use it.
   */
  @Override
  public void togglePlay() {
    if (playingBool) {
      pause();
    } else {
      play();
    }
  }


  /*********************************************************************/
  /***********************NOT IMPLEMENTED*******************************/
  /*********************************************************************/
  @Override
  public void addKeyListener(KeyListener listener) {

  }

  @Override
  public void addActionListener(ActionListener listener) {

  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {

  }

  /*******************************************************************/

  @Override
  public void addMetaListener(MetaEventListener metaListener) {
    this.sequencer.addMetaEventListener(metaListener);
  }

  @Override
  public void setCurrentBeat(int beat) {
    sequencer.setMicrosecondPosition(beat * 6000);
  }


  public int getCurrentBeat() {
    return Math.toIntExact(sequencer.getMicrosecondPosition()) / 6000;
  }

  @Override
  public boolean isPlayingBool() {
    return playingBool;
  }

  @Override
  public void play() {
    try {
      sequencer.setSequence(sequence);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    sequencer.setTempoInMPQ(tempo);
    sequencer.start();
    playingBool = true;
  }

  @Override
  public void pause() {
    sequencer.stop();
    try {
      sequencer.setSequence(sequence);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    sequencer.setTempoInMPQ(tempo);
    playingBool = false;
  }

  @Deprecated
  @Override
  public void changeVolume(int newVolume) {
    model.changeVolume(newVolume);
  }

  public MidiView getView() {
    return this;
  }

  @Override
  public void changeTempo(int newtempo) {
    tempo = newtempo;
    try {
      createMIDITrack();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }
}