package cs3500.music.controller;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.otherView.CompositeView;

import java.util.TimerTask;

/**
 *  Timer claas that implements the built-in TimerTask and keep track of the
 *  timing of the Views
 */
public class ModelTimer extends TimerTask {

  private final MusicEditorModel model;
  private final CompositeView view;

  public ModelTimer(MusicEditorModel model, CompositeView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void run() {
    if (model.getPlayingStatus()) {
      if (this.model.getCurrentBeat() <= this.model.getMaxFinishTime() + 1) {
        this.view.initialize();
      } else {
        this.view.reinitialize();
      }
      this.view.initialize();
    }
  }
}
