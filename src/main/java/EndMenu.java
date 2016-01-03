package main.java;

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
public class EndMenu extends JFrame  {
    String message = "";
    public EndMenu(String message) {
        this.message = message;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new BackgroundPanel(), gbc);

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
            return new Dimension(300, 290);
        }

        @Override
        protected void paintComponent(Graphics g) {
            BufferedImage image=null;
            try {
                image = ImageIO.read(new File("src/main/resources/background.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.paintComponent(g);
            g.drawImage(image, 0, 0, 300, 300, 0, 0, 525, 558, null); // see javadoc for more info on the parameters
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial",2,20));
            g.drawString(message, 100, 150);
        }
    }


    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EndMenu menu = new EndMenu("NECO NAPIS");
            }
        });

    }

}
