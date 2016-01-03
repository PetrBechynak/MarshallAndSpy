package main.java;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by petr on 17.12.15.
 */
public class Menu extends JFrame  {

    public Menu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new BackgroundPanel(), gbc);

        BottomPanel bottomPanel = new BottomPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(bottomPanel, gbc);

        Menu.StartButton startButton = new Menu.StartButton();
        bottomPanel.add(startButton);
        startButton.addActionListener(new ActionStart());

        Menu.EndButton endButton = new Menu.EndButton();
        bottomPanel.add(endButton);
        endButton.addActionListener(new ActionEnd());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Handle the CLOSE button
        setTitle("MENU - Marsal a spion");  // this JFrame sets the title
        pack();              // Either pack() the components; or setSize()
        setVisible(true);    // this JFrame show
    }


    public class BackgroundPanel extends JPanel {

        public BackgroundPanel() {
            setBackground(Color.RED);
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 300);
        }
        @Override
        protected void paintComponent(Graphics g) {
            BufferedImage image=null;
            try {
                image = ImageIO.read(new File("src/main/resources/introImage.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.paintComponent(g);
            g.drawImage(image, 0, 0, 300, 300, 0, 0, 525, 558, null); // see javadoc for more info on the parameters
        }
    }

    public class BottomPanel extends JPanel {
        public BottomPanel()
        {
            setBackground(Color.BLACK);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 50);
        }
    }

    static class ActionStart implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("You clicked the button " + e.toString());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Board board = new Board();
                    new DrawingEngine(board); // Let the constructor do the job
                }
            });

        }
    }

    static class ActionEnd implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    static class StartButton extends JButton {
        public StartButton() {
            this.setText("Play");
            this.setSize(100, 100);
        }
    }

    static class EndButton extends JButton {
        public EndButton() {
            this.setText("End");
            this.setSize(100,100);
        }
    }

    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        BasicConfigurator.configure(new ConsoleAppender(
                new PatternLayout("%d{HH:mm:ss,SSS} %c %x %M - %m%n")));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Menu menu = new Menu();
            }
        });

    }

}
