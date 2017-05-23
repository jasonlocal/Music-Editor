package cs3500.music.otherView;

import cs3500.music.ModelAdaptor.IPlayable;

import javax.sound.midi.MetaEventListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import static cs3500.music.model.Note.pitchToString;

class GuiViewImpl extends JFrame implements GuiView {

  private int startX = 40;
  private int startY = 40;
  private int measureSize = 80;
  private int currentBeat;

  private final ViewModel model;
  private GuiPanel jGrid;
  private JScrollPane jScrollPane;

  public GuiViewImpl(ViewModel model) {
    this.model = model;
    super.setTitle("Music Editor CS3500");
    currentBeat = 0;
    this.jGrid = new GuiPanel();
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.jScrollPane = new JScrollPane(jGrid, JScrollPane
            .VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.jScrollPane.setWheelScrollingEnabled(true);
    this.jGrid.setPreferredSize(getPreferredSize());
    this.setFocusable(true);
    this.add(jScrollPane);
    this.jGrid.setPreferredSize(
            new Dimension((startX + 50 + (measureSize / 4 * model.getLastBeat())),
                    (startY + 50 + (measureSize / 4 *
                            (model.getHighestPlayable().getPitch() -
                                    model.getLowestPlayable().getPitch())))));
    this.pack();
    jGrid.setIgnoreRepaint(false);
  }


  @Override
  public void initialize() {
    this.setVisible(true);
  }


  /**
   * Set the currentbeat field for this view.
   * @param beat the current beat
   */
  public void setCurrentBeat(int beat) {
    this.currentBeat = beat;
  }

  /**
   * Retreive the current beat field for this view.
   * @return the current beat of the guiview
   */
  public int getCurrentBeat() {
    return this.currentBeat;
  }

  /**
   * Returns this view object.
   * @return this
   */
  public View getView() {
    return this;
  }

  @Override
  public boolean isFocusable() {
    return true;
  }

  @Override
  public void addActionListener(ActionListener listener) {

  }

  @Override
  public void addMetaListener(MetaEventListener metaListener) {

  }

  @Override
  public void addMouseListener(MouseListener ml) {
    jGrid.addMouseListener(ml);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void setScrollPos(boolean horiz, int scrollUnits) {
    if (horiz){
      jScrollPane.getHorizontalScrollBar().setValue(
              jScrollPane.getHorizontalScrollBar().getValue()+scrollUnits);
    }
    else {
      jScrollPane.getVerticalScrollBar().setValue(
              jScrollPane.getVerticalScrollBar().getValue()+scrollUnits);
    }
  }

  @Override
  public void reinitialize(){
    jGrid.repaint();
  }


  /**
   * GuiPanel class contains methods to paint the grid, the playables, and playhead, and the text
   */
  private class GuiPanel extends JPanel {

    private int startX = 40;
    private int startY = 40;
    private int measureSize = 80;
    private double width;
    private double height;

    int current;
    Dimension screenSize;

    GuiPanel() {
      screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      width = screenSize.getWidth();
      height = screenSize.getHeight();
      this.setSize(Math.toIntExact(Math.round(width)), Math.toIntExact(Math.round(height)));
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      paintPlayable(g);
      paintGrid(g);
      paintPlayhead(g);
      paintPitches(g);

    }

    /**
     * Paints the grid of for the score.
     * @param g graphics variable
     */
    private void paintGrid(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(Color.white);
      g2.setPaint(Color.black);
      //obtain value from lowest pitch to highest pitch
      int COLUMN_SIZE = model.getHighestPlayable().getPitch() -
              model.getLowestPlayable().getPitch() + 1;
      int ROWS_NUM = ((model.getLastBeat() / 4)) + 1;
      //create grid
      for (int i = 0; i < ROWS_NUM; i++) {
        for (int k = 0; k < COLUMN_SIZE; k++) {
          g2.draw(new Rectangle2D.Double(startX + (measureSize * i),
                  startY + ((measureSize / 4) * k),
                  measureSize,
                  measureSize / 4));
        }
        //displaying measure numbers
        if (i % 4 == 0) {
          g2.drawString(Integer.toString(i * 4), startX + (measureSize * i), startY - 3);
        }
      }
    }

    /**
     * Paints the Playables on the score.
     * @param g graphics variable
     */
    private void paintPlayable(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(Color.white);

      int yPos;
      int emptySpace = 0;

      for (int i = 0; i < model.getLastBeat(); i++) {
        if (model.containsBeat(i)) {
          Collection<IPlayable> playableCollection = model.getPlayablesAtBeat(i);
          for (IPlayable note : playableCollection) {
            yPos = model.getHighestPlayable().getPitch() - note.getPitch();
            if (note.getStartBeat() == i) {
              g2.setPaint(Color.black);
              g2.fill(new Rectangle2D.Double(startX + ((measureSize / 4) * i),
                      startY + ((measureSize / 4) * yPos) + emptySpace,
                      measureSize / 4,
                      measureSize / 4));
            } else {
              g2.setPaint(Color.green);
              g2.fill(new Rectangle2D.Double(startX + ((measureSize / 4) * i),
                      startY + ((measureSize / 4) * yPos) + emptySpace,
                      measureSize / 4,
                      measureSize / 4));
            }
          }
        }
      }
    }


    /**
     * Paints the string of the each pitch used in this piece.
     * @param g graphics variable
     */
    private void paintPitches(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(Color.white);
      g2.setPaint(Color.black);
      int k = 0;
      //create pitch names
      for (int i = model.getHighestPlayable().getPitch();
           i >= model.getLowestPlayable().getPitch(); i--) {
        g2.drawString(pitchToString(i), 0, (startY + 15) + ((measureSize / 4) * k));
        if ((i % 12) == 0){
          g2.setStroke(new BasicStroke(3));
          g2.drawLine(startX, (startY + 20) + ((measureSize / 4) * k),
                  startX + ((measureSize / 4) * (model.getLastBeat()+1)),
                  (startY + 20) + ((measureSize / 4) * k));
          g2.setStroke(new BasicStroke(1));
        }
        k++;
      }
    }

    /**
     * Paints the playhead on the score, and adjusts the jScrollPane to always align with
     * the playhead
     * @param g graphics variable
     */
    private void paintPlayhead(Graphics g) {
      this.current = currentBeat;
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(Color.white);
      g2.setColor(Color.red);
      int COLUMN_SIZE = model.getHighestPlayable().getPitch() -
              model.getLowestPlayable().getPitch() + 1;
      g.drawLine(startX + current, startY, startX + current,
              (COLUMN_SIZE + 2) * (80 / 4));
      jScrollPane.getHorizontalScrollBar().setValue(current);
    }
  }
}