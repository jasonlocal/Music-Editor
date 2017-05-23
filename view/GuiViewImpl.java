package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class GuiViewImpl extends javax.swing.JFrame implements GuiView {

  private static final int ViewportHeight = 330;
  private static final int WindowWidth = 1080;
  private static final int WindowHeight = 400;

  //The left most time. Buttons can change this.
  private int leftTime;
  //The right most time, buttons can change this.
  private int rightTime;

  //The top and bottom pitch displayed that can be changed by the buttons.
  private int topPitch;
  private int bottomPitch;

  //List of all of the pitch and second labels. This is so
  //the cs3500.music.view relabels these labels rather than creating new ones.
  private ArrayList<JLabel> pitches;
  private ArrayList<JLabel> seconds;

  //The melody being displayed. This gets updated by the Subject
  // in a observer-subject relationship
  private MusicEditorModel melody;

  public GuiViewImpl(MusicEditorModel model) {
    super("Music Editor");

    //Register this cs3500.music.view with the observer so it can start getting
    // updates.
    model.registerViewObserver(this);

    //JFrame settings
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);
    setSize(WindowWidth, WindowHeight);
    setResizable(false);

    seconds = new ArrayList<>();
    pitches = new ArrayList<>();

    //Initialize all of the variables so the right information can be displayed
    //on the screen. This is all here so that the user can scroll to see the
    //entire composition.
    leftTime = 0;
    rightTime = 60;

    topPitch = this.melody.getMinPitch();
    bottomPitch = this.melody.getMinPitch();

    //Calculate number of bars to display.
    int numBars = this.melody.getMaxPitch() - this.melody.getMinPitch();
    if (numBars >= 15) {
      numBars = 15;
    }
    bottomPitch += numBars;


    //Create the button pad so the user can scroll the composition.
    JButton downBtn = new JButton("\\/");
    downBtn.setBounds(WindowWidth / 2-40, WindowHeight - 80, 40, 40);
    downBtn.addActionListener(e -> {
      topPitch++;
      bottomPitch++;

      if (bottomPitch > melody.getMaxPitch()) {
        topPitch--;
        bottomPitch--;
      }

      repaint();
    });
    JButton rightBtn = new JButton(">");
    rightBtn.setBounds(WindowWidth/2+5, WindowHeight - 80, 45, 40);
    rightBtn.addActionListener(e -> {
      leftTime += 4;
      rightTime += 4;
      repaint();

    });
    JButton leftBtn = new JButton("<");
    leftBtn.setBounds(WindowWidth/2 - 90, WindowHeight - 80, 45, 40);
    leftBtn.addActionListener(e -> {
      leftTime -= 4;
      rightTime -= 4;

      if (leftTime < 0)
      {
        leftTime += 4;
        rightTime += 4;
      }

      repaint();
    });
    JButton upBtn = new JButton("/\\");
    upBtn.setBounds(WindowWidth / 2-40, WindowHeight - 125, 40, 40);
    upBtn.addActionListener(e -> {
      topPitch--;
      bottomPitch--;

      if (topPitch < melody.getMinPitch())
      {
        topPitch++;
        bottomPitch++;
      }
      repaint();
    });

    //--end button construction

    //add all the buttons to the frame.
    add(rightBtn);
    add(downBtn);
    add(leftBtn);
    add(upBtn);

    setVisible(true);

  }

  /**
   * This function draws the grid boxes onto the frame
   * @param g the graphics object to draw onto
   */
  private void drawBoxes(Graphics2D g)
  {
    g.setPaint(Color.black);

    int finishLine = 64;
    finishLine -=  finishLine % 4;
    finishLine = (finishLine / 4) * 64;
    finishLine += 40;

    //vertical lines
    for (int x = 40; x <= finishLine; x += 64)
    {
      Line2D line = new Line2D.Float(x, 40, x, ViewportHeight -34);
      g.draw(line);
    }

    //horizontal lines
    for (int y = 40; y < ViewportHeight - 32; y += 16)
    {
      Line2D line = new Line2D.Float(40, y, finishLine, y);
      g.draw(line);
    }
  }

  /**
   * Draws the actual composition onto the frame
   * @param g the graphics object to draw onto.
   */
  private void drawMusic(Graphics2D g) {
    int width = 16;
    int height = 16;

    int pitch = topPitch;
    for (int y = 40; y <= ViewportHeight - 48; y += 16) {
      ArrayList<NoteModel> notes = this.melody.getNotesByPitch(pitch);

      //Loop through the time intervals for each pitch, drawing them onto
      // the screen
      for (NoteModel n : notes) {
        g.setPaint(Color.black);

        //Calculates where to start painting based on how far the user has
        // scrolled.
        int startPaint = (n.getStartTime()-this.leftTime)*16 + 40;

        g.fill(new Rectangle2D.Double(startPaint, y,width,height));

        g.setPaint(Color.green);

        if (n.getStartTime() != n.getFinishTime()) {
          int startGreen = n.getStartTime() + 1 - this.leftTime;
          int finishGreen = n.getFinishTime()+1-this.leftTime;
          startGreen = startGreen*16 + 40;
          finishGreen = finishGreen*16 + 40;
          finishGreen = finishGreen - startGreen;

          g.fill(new Rectangle2D.Double(startGreen, y, finishGreen, height));

          g.fill(new Rectangle2D.Double(startGreen, y, finishGreen, height));
        }
      }

      pitch++;
    }
  }

  /**
   * Draws the time information onto the screen. This can change
   * if the user scrolls
   * @param g graphics object to draw onto
   */
  private void drawSeconds(Graphics2D g) {
    g.setPaint(Color.black);

    int secondsX = 30;
    int secondsY = ViewportHeight - 55;
    if (seconds.isEmpty()) {
      for (int seconds = leftTime; seconds <= rightTime; seconds += 4)
      {
        JLabel label = new JLabel(Integer.toString(seconds));
        label.setBounds(secondsX,secondsY,30,15);
        secondsX += 64;
        add(label);
        this.seconds.add(label);
      }
    }
    else {
      int index = 0;

      for (int seconds = leftTime; seconds <= rightTime; seconds += 4)
      {
        this.seconds.get(index).setText(Integer.toString(seconds));

        index++;
      }
    }

  }

  /**
   * Draws the note labels that appear on the left side of the window
   * @param g graphics object to draw
   */
  private void drawPitch(Graphics2D g)
  {
    int labelX = 5;
    int labelY = 13;
    int labelW = 22;
    int labelH = 10;
    int offset = 6;

    if (pitches.isEmpty()) //create new note labels
    {
      for (int pitch = topPitch; pitch <= bottomPitch; pitch++)
      {
        String note = MusicEditorModel.pitchToNote(pitch);

        JLabel label = new JLabel(note);
        label.setBounds(labelX, labelY, labelW, labelH);
        add(label);

        labelY += labelH+offset;

        pitches.add(label);
      }
    }
    else //relabel notes
    {
      int index = 0;

      for (int pitch = topPitch; pitch <= bottomPitch; pitch++) {
        String note = MusicEditorModel.pitchToNote(pitch);

        pitches.get(index).setText(note);

        index++;
      }
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;

    this.setLayout(null);

    drawMusic(g2d);
    drawBoxes(g2d);
    drawSeconds(g2d);
    drawPitch(g2d);
  }

  @Override
  public void render() {
  }

  public void update(MusicEditorModel melody) {
    this.melody = melody;
  }

  @Override
  public int getClickedBeat(int xCoordinate) {
    return ((xCoordinate - 40)/16) -1 + this.leftTime;
  }

  @Override
  public int getClickedPitch(int yCoordinate) {
    return (yCoordinate-40)/16;
  }
}
