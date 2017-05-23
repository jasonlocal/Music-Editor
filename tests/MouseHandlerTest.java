package cs3500.music.tests;

import cs3500.music.controller.MouseHandler;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * Test class for MouseHandler
 */
public class MouseHandlerTest {
  @Test
  public void testMouseListener() {
    Appendable str = new StringBuilder();
    Map<Integer, Consumer<MouseEvent>> onClick = new HashMap<>();
    onClick.put(MouseEvent.BUTTON1, (MouseEvent me) -> {
      try {
        str.append("Click: ").append("B1 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onClick.put(MouseEvent.BUTTON2, (MouseEvent me) -> {
      try {
        str.append("Click: ").append("B2 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onClick.put(MouseEvent.BUTTON3, (MouseEvent me) -> {
      try {
        str.append("Click: ").append("B3 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    Map<Integer, Consumer<MouseEvent>> onPress = new HashMap<>();
    onPress.put(MouseEvent.BUTTON1, (MouseEvent me) -> {
      try {
        str.append("Press: ").append("B1 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onPress.put(MouseEvent.BUTTON2, (MouseEvent me) -> {
      try {
        str.append("Press: ").append("B2 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onPress.put(MouseEvent.BUTTON3, (MouseEvent me) -> {
      try {
        str.append("Press: ").append("B3 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    Map<Integer, Consumer<MouseEvent>> onRelease = new HashMap<>();
    onRelease.put(MouseEvent.BUTTON1, (MouseEvent me) -> {
      try {
        str.append("Release: ").append("B1 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    onRelease.put(MouseEvent.BUTTON2, (MouseEvent me) -> {
      try {
        str.append("Release: ").append("B2 at ").append(
            Integer.toString(me.getX())).append(", ")
            .append(Integer.toString(me.getY())).append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    MouseListener ml = new MouseHandler(onClick, onPress, onRelease);
    assertEquals(str.toString(), "");
    ml.mouseClicked(new MouseEvent(new JPanel(), 0, 0, 0, 55, 55, 1, false,
        MouseEvent.BUTTON1));
    assertEquals(str.toString(), "Click: B1 at 55, 55\n");
    ml.mousePressed(new MouseEvent(new JPanel(), 0, 0, 0, 55, 55, 1, false,
        MouseEvent.BUTTON1));
    assertEquals(str.toString(), "Click: B1 at 55, 55\n" +
        "Press: B1 at 55, 55\n");
    ml.mouseReleased(new MouseEvent(new JPanel(), 0, 0, 0, 5, 5, 1, false,
        MouseEvent.BUTTON2));
    assertEquals(str.toString(), "Click: B1 at 55, 55\n" +
        "Press: B1 at 55, 55\n" +
        "Release: B2 at 5, 5\n");
    ml.mouseReleased(new MouseEvent(new JPanel(), 0, 0, 0, 5, 5, 1, false,
        MouseEvent.BUTTON3));
    assertEquals(str.toString(), "Click: B1 at 55, 55\n" +
        "Press: B1 at 55, 55\n" +
        "Release: B2 at 5, 5\n");
    ml.mouseReleased(new MouseEvent(new JPanel(), 0, 0, 0, 5, 35, 1, false,
        MouseEvent.BUTTON1));
    ml.mouseClicked(new MouseEvent(new JPanel(), 0, 0, 0, 15, 5, 1, false,
        MouseEvent.BUTTON3));
    ml.mousePressed(new MouseEvent(new JPanel(), 0, 0, 0, 3, 25, 1, false,
        MouseEvent.BUTTON3));
    assertEquals(str.toString(), "Click: B1 at 55, 55\n" +
        "Press: B1 at 55, 55\n" +
        "Release: B2 at 5, 5\n" +
        "Release: B1 at 5, 35\n" +
        "Click: B3 at 15, 5\n" +
        "Press: B3 at 3, 25\n");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException1() {
    new MouseHandler(null, new HashMap<>(), new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new MouseHandler(new HashMap<>(), null, new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    new MouseHandler(new HashMap<>(), new HashMap<>(), null);
  }
}
