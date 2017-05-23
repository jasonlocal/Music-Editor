package cs3500.music.controller;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.NoteModel;
import cs3500.music.otherView.CompositeView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.function.Consumer;

;

/**
 * Controller for GuiView type views.
 */
public class ControllerImpl implements Controller {
  private final CompositeView view;
  private final MusicEditorModel model;
  private final Timer timer;
  private boolean shiftPress;
  private int instNum;
  private boolean isPaused;


  public ControllerImpl(CompositeView view, MusicEditorModel model) {
    this.view = view;
    this.model = model;
    this.timer = new Timer();
    this.shiftPress = false;
    this.instNum = 0;
    this.isPaused = false;
    setKeyHandler();
    setMouseHandler();
  }

  @Override
  public void run() throws IOException {
    this.setKeyHandler();
    this.setMouseHandler();
    this.view.initialize();
    this.timer.scheduleAtFixedRate(
        new ModelTimer(this.model, this.view), 100,
        this.model.getTempo() / 1000);
  }

  private void setKeyHandler() {
    HashMap<Integer, Runnable> onPress = new HashMap<>();
    HashMap<Integer, Runnable> onRelease = new HashMap<>();
    HashMap<Integer, Runnable> onTyped = new HashMap<>();
    onPress.put(82,
        () -> {
          for (int i = 0; i < model.getMaxFinishTime(); i++) {
            model.getNotesPlayingAt(i).forEach(model::removeNote);
          }
          view.initialize();
        });

    onPress.put(16,
        () -> {
          this.shiftPress = true;
        });
    onRelease.put(16,
        () -> {
          this.shiftPress = false;
        });
    onPress.put(48,
        () -> {
          this.instNum = 0;
        });
    onRelease.put(48,
        () -> {
          if (this.instNum == 0) {
            this.shiftPress = false;
          }
        });
//Press key Num 1 to pause
    onTyped.put(49,
        ()-> {
          if (this.isPaused) {
            this.isPaused = true;
            this.timer.cancel();
          } else {
            this.isPaused = false;
            this.timer.scheduleAtFixedRate(new ModelTimer(this.model,
                    this.view), 100,
                this.model.getTempo() / 1000);
          }
        });
    view.addKeyListener(
        new KeyboardHandler(onPress, onRelease, onTyped));
  }

  private void setMouseHandler() {
    HashMap<Integer, Consumer<MouseEvent>> onClick = new HashMap<>();
    HashMap<Integer, Consumer<MouseEvent>> onPress = new HashMap<>();
    HashMap<Integer, Consumer<MouseEvent>> onRelease = new HashMap<>();

    onPress.put(MouseEvent.BUTTON1,
        (MouseEvent me) -> {
          if (shiftPress) {
            int x = me.getX();
            int beat = (x - 40) / 80;
            int y = me.getY();
            int pitch = (y - 40) / 20;
            ArrayList<NoteModel> temp = this.model.getNotesByBeatAndPitch(beat,
                pitch);
            for (NoteModel noteModel : temp) {
              this.model.removeNote(noteModel);
            }

          } else {
            NoteModel tempNote;
            int x = me.getX();
            int beat = (x - 40) / 80;
            int y = me.getY();
            int pitch = (y - 40) / 20;
            ArrayList<NoteModel> temp = this.model.getNotesByBeatAndPitch(beat,
                pitch);
            if (temp.size() > 1) {
              throw new UnknownError("Only one Note shall be selected");
            }

            tempNote = temp.get(0);

            JPanel popupForNoteEdit = new JPanel();
            JTextField startTF = new JTextField("Starting beat");
            JTextField endTF = new JTextField("end beat");
            JTextField instrumentTF = new JTextField("instrument");
            JTextField pitchTF = new JTextField("pitch");
            JTextField volumeTF = new JTextField("volume");

            JButton enter = new JButton();
            enter.setFocusable(true);
            enter.addActionListener(e -> {
              model.removeNote(tempNote);

              int start;
              {
                try {
                  start = Integer.parseInt(startTF.getText());
                } catch (NumberFormatException ex) {
                  start = tempNote.getStartTime();
                }
              }

              int end;
              {
                try {
                  end = Integer.parseInt(endTF.getText());
                } catch (NumberFormatException ex) {
                  end = tempNote.getFinishTime();
                }
              }

              int instrument;
              {
                try {
                  instrument = Integer.parseInt(instrumentTF.getText());
                } catch (NumberFormatException ex) {
                  instrument = tempNote.getInstrument();
                }
              }

              int pitchN;
              {
                try {
                  pitchN = Integer.parseInt(pitchTF.getText());
                } catch (NumberFormatException ex) {
                  pitchN = tempNote.getPitch();
                }
              }

              int volume;
              {
                try {
                  volume = Integer.parseInt(volumeTF.getText());
                } catch (NumberFormatException ex) {
                  volume = tempNote.getVolume();
                }
              }
              NoteModel newNote = new Note(start, end, instrument, pitchN,
                  volume);
              try {
                model.addNotes(newNote);
              } catch (MusicEditorImpl.InvalidNoteException e1) {
                e1.printStackTrace();
              }
            });

            popupForNoteEdit.add(startTF);
            popupForNoteEdit.add(endTF);
            popupForNoteEdit.add(instrumentTF);
            popupForNoteEdit.add(pitchTF);
            popupForNoteEdit.add(volumeTF);
            popupForNoteEdit.add(enter);
            popupForNoteEdit.setVisible(true);
          }
        });

    view.addMouseListener(
        new MouseHandler(
            onClick,
            onPress,
            onRelease));
  }
}

