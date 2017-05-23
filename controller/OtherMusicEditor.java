package cs3500.music;

import cs3500.music.controller.Controller;
import cs3500.music.controller.ControllerImpl;
import cs3500.music.ModelAdaptor.MusicEditorImpl;
import cs3500.music.ModelAdaptor.MusicEditorModel;
import cs3500.music.otherView.CompositeView;
import cs3500.music.otherView.View;
import cs3500.music.otherView.ViewFactory;
import cs3500.music.otherView.ViewModelWrapper;
import cs3500.music.util.MusicReader;

import java.io.*;

/**
 * Runner for the other group's view with controller
 */
public class OtherMusicEditor {
  public static void main(String args[]) throws FileNotFoundException {
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
    MusicEditorImpl.Builder builder =
        new MusicEditorImpl.Builder();
    MusicEditorModel model = MusicReader.parseFile(new FileReader(file),
        builder);

    View view;
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
        view = ViewFactory.getView(viewtype, new ViewModelWrapper(model));
        break;
      }
      catch (IllegalArgumentException ignored) {
        System.out.print("Unrecognized View Type. Legal View Types: visual, " +
            "console, midi");
      }
    }
    if (viewtype.equals("composite")) {
      Controller controller = new ControllerImpl((CompositeView)view, model);
      try {
        controller.run();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      view.initialize();
    }
  }
}
