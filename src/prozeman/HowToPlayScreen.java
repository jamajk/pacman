package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class HowToPlayScreen extends JPanel {
    public HowToPlayScreen(pWindow parent) {

        setBackground(Color.darkGray);
        try {
            Image image = ImageIO.read(new FileInputStream("img/Logo.png"));
            JLabel imgLabel = new JLabel(new ImageIcon(image));
            imgLabel.setAlignmentX(CENTER_ALIGNMENT);
            add(imgLabel);
            add(Box.createVerticalStrut(60));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel textLabel1 = new JLabel();
        textLabel1.setFont(Config.fontSmall);
        textLabel1.setForeground(Color.white);
        textLabel1.setText("<html>The goal of the game is to eat the pellets<br>and score points.<br>This task is made harder by ghosts<br>roaming around and taking your lives.</html>");
        add(textLabel1);
        add(Box.createVerticalStrut(20));

        JLabel textLabel2 = new JLabel();
        textLabel2.setFont(Config.fontSmall);
        textLabel2.setForeground(Color.white);
        textLabel2.setText("<html>Controls:<br><br>Arrow keys to move.<br>Ctrl to stop your character.<br>Press P or Escape to pause.</html>");
        add(textLabel2);
        add(Box.createVerticalStrut(20));

        TextButton backButton = new TextButton("Back");
        add(backButton);
        add(Box.createVerticalStrut(20));


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.toMain();
            }
        });
    }
}
