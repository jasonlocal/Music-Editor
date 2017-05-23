package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;

/**
 * Observer interface that all views will implement.
 */
public interface ViewObserver extends TotalView {
  /**
   * Renders the current state of the cs3500.music.MusicEditorImpl.
   * cs3500.music.Note, this doesn't just assume graphical rendering. Views
   * will also call and implement this function for actually playing audio as
   * well.
   */
  void render();

  /**
   * This is invoked by the Subject, and the View is responsible
   * for responding to this update.
   *
   * @param melody melody data to update cs3500.music.view with
   */
  void update(MusicEditorModel melody);
}

