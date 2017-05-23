package cs3500.music.otherView;

/**
 * Factory that creates the correct view.
 */
public class ViewFactory {
  public static View getView(String request, ViewModel model) {
    switch (request) {
      case("gui"):
        return new GuiViewImpl(model);
      case ("console"):
        return new ConsoleViewImpl(model);
      case ("midi"):
        return new MidiViewImpl(model);
      case ("composite"):
        return new CompositeView(model);
      default:
        throw new IllegalArgumentException("There exists no view with that name");
    }
  }
}