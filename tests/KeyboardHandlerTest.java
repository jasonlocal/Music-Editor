package cs3500.music.tests;


import cs3500.music.controller.KeyboardHandler;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class for Testing MouseHandler
 */
public class KeyboardHandlerTest {
  @Test
  public void testKeyListener() {
    Appendable str = new StringBuilder();
    Map<Integer, Runnable> onPress = new HashMap<>();
    onPress.put(KeyEvent.VK_R, () -> {
      try {
        str.append("Press: ").append("R").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onPress.put(KeyEvent.VK_P, () -> {
      try {
        str.append("Press: ").append("P").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onPress.put(KeyEvent.VK_LEFT, () -> {
      try {
        str.append("Press: ").append("Left Arrow").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    Map<Integer, Runnable> onRelease = new HashMap<>();
    onRelease.put(KeyEvent.VK_R, () -> {
      try {
        str.append("Release: ").append("R").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onRelease.put(KeyEvent.VK_P, () -> {
      try {
        str.append("Release: ").append("P").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onRelease.put(KeyEvent.VK_LEFT, () -> {
      try {
        str.append("Release: ").append("Left Arrow").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    Map<Integer, Runnable> onType = new HashMap<>();
    onType.put(KeyEvent.VK_R, () -> {
      try {
        str.append("Type: ").append("R").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onType.put(KeyEvent.VK_P, () -> {
      try {
        str.append("Type: ").append("P").append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    KeyListener kl = new KeyboardHandler(onPress, onRelease, onType);
    assertEquals(str.toString(), "");
    kl.keyPressed(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_R, 'r'));
    assertEquals(str.toString(), "Press: R\n");
    kl.keyTyped(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_R, 'r'));
    assertEquals(str.toString(), "Press: R\nType: R\n");
    kl.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_R, 'r'));
    assertEquals(str.toString(), "Press: R\nType: R\nRelease: R\n");
    kl.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_P, 'P'));
    assertEquals(str.toString(), "Press: R\nType: R\nRelease: R\nRelease: P\n");
    kl.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_LEFT, 'r'));
    assertEquals(str.toString(), "Press: R\nType: R\nRelease: R" +
        "\nRelease: P\nRelease: Left Arrow\n");
    kl.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_LEFT, 'r'));
    kl.keyTyped(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_UP, 'r'));
    kl.keyPressed(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_P, 'r'));
    kl.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_R, 'r'));
    kl.keyTyped(new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_G, 'r'));
    assertEquals(str.toString(), "Press: R\nType: R\nRelease: R" +
        "\nRelease: P\nRelease: Left Arrow\nRelease: Left Arrow" +
        "\nPress: P\nRelease: R\n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException1() {
    new KeyboardHandler(null, new HashMap<>(), new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new KeyboardHandler(new HashMap<>(), null, new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    new KeyboardHandler(new HashMap<>(), new HashMap<>(), null);
  }
}
