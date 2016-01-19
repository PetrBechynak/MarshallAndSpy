package main.java;

/**
 * Created by petr on 9.12.15.
 */

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Custom Drawing Code Template */
// Graphics application extends JFrame
public class DrawingEngine extends JFrame {
    // Constants
    static Logger logger = Logger.getLogger(DrawingEngine.class);
    public static final int FIGURE_SIZE = 50;
    public static final int FIGURE_BORDER = 2;
    public static final int CANVAS_WIDTH  = FIGURE_SIZE*12;
    public static final int CANVAS_HEIGHT = FIGURE_SIZE*12;
    public Board board;

    // Declare an instance the drawing canvas (extends JPanel)
    private DrawCanvas canvas;

    public DrawingEngine(){}

    /** Constructor to set up the GUI components */
    public DrawingEngine(Board board) {
        logger.debug("start vole");
        canvas = new DrawCanvas();    // Construct the drawing canvas
        this.board=board;
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or
        // setContentPane(canvas);
        cp.addMouseListener(new CustomMouseListener());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
        this.pack();              // Either pack() the components; or setSize()
        this.setTitle("Marsal a spion");  // this JFrame sets the title
        this.setVisible(true);    // this JFrame show
        logger.debug("konec konstruktoru");
    }

    /**
     * Define a inner class called DrawCanvas which is a JPanel used for custom drawing
     */
    private class DrawCanvas extends JPanel {
        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
               board.draw(g);
        }
    }

    class CustomMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            //System.out.println("Mouse Clicked: ("
            //        + e.getX() +", "+e.getY() +")");
            logger.debug("click!");
            board.manageClick(e.getX(), e.getY());
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /** Entry main method */
    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Board board = new Board();
                board.generateInitialFigures(new Player(Player.Type.HUMAN));
                board.generateInitialFigures(new Player(Player.Type.COMPUTER));
                new DrawingEngine(board); // Let the constructor do the job
            }
        });
    }
}
