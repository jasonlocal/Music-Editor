package cs3500.music;
import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.*;

import javax.sound.midi.InvalidMidiDataException;
import java.io.*;

public class MusicEditor {

  /**
   * Factory for creating views based on given parameters.
   * @param viewClass the name of the cs3500.music.view
   * @param model the cs3500.music.model to hook the cs3500.music.view to
   * @return the created cs3500.music.view
   */
  private static View viewFactory(String viewClass,
                                             MusicEditorModel model) throws IOException {
    switch (viewClass) {
      case "console":
        return new ConsoleViewImpl(model);
      case "visual":
        return new GuiViewImpl(model);
      case "midi":
        return new MidiViewImpl(model);
      case "composite":
        return new CompositeViewImpl(model);
      default:
        throw new IllegalArgumentException("Unrecognized View Type.");
    }
  }

  public static void main(String[] args) throws IOException,
      InvalidMidiDataException, RuntimeException {
    String filename = "";
    String viewtype = "";

    while(true) {
      System.out.print("please enter the name of the music");
      System.out.println();
      try {
        BufferedReader bufferRead = new BufferedReader(new
            InputStreamReader(System.in));
        filename = bufferRead.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (filename.equals("quite")) {
        System.out.print("\n System Exit.");
        System.exit(0);
        break;
      }
      if (filename.equals("mary-little-lamb") || filename.equals("mystery-1")
          || filename.equals("mystery-2") || filename.equals("mystery-3")) {
        filename += ".txt";
        System.out.print(filename + " loaded. \n");
        break;
      } else {
        System.out.print("No Such file on record. \n");
      }
    }

    File file = new File(filename);
    MusicEditorImpl.Builder builder = new MusicEditorImpl.Builder();
    MusicEditorModel model = MusicReader.parseFile(new FileReader(file),
        builder);

    ViewObserver view;
    while(true) {
      System.out.print("please enter the type of view: ");
      System.out.println();
      try {
        BufferedReader bufferRead = new BufferedReader(new
            InputStreamReader(System.in));
        viewtype = bufferRead.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        view = viewFactory(viewtype, model);
        break;
      }
      catch (IllegalArgumentException ignored) {
        System.out.print("Unrecognized View Type. Legal View Types: visual, " +
            "console, midi");
      }
    }
    view.render();
  }
}
