package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;

/**
 * the interface View has a render method
 * for different classes to implement
 */
public interface View extends ViewObserver
{

  /**
   * This is invoked by the Subject, and the View is responsible
   * for responding to this update.
   *
   * @param melody melody data to update cs3500.music.view with
   */
  void update(MusicEditorModel melody);

}
