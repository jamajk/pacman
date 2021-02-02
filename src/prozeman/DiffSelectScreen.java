package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiffSelectScreen extends JPanel {
    public DiffSelectScreen(pWindow parent) {

        setBackground(Color.darkGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel textLabel1 = new JLabel();
        textLabel1.setFont(Config.fontLarge);
        textLabel1.setForeground(Color.lightGray);
        textLabel1.setText("Select Difficulty");
        add(textLabel1);
        add(Box.createVerticalStrut(20));

        GameUIButton easyButton = new GameUIButton("Easy");
        add(easyButton);
        add(Box.createVerticalStrut(5));

        JLabel textLabel2 = new JLabel();
        textLabel2.setFont(Config.fontSmall);
        textLabel2.setForeground(Color.white);
        textLabel2.setText("Slow pace, lots of health");
        add(textLabel2);
        add(Box.createVerticalStrut(20));

        GameUIButton mediumButton = new GameUIButton("Medium");
        add(mediumButton);
        add(Box.createVerticalStrut(5));

        JLabel textLabel3 = new JLabel();
        textLabel3.setFont(Config.fontSmall);
        textLabel3.setForeground(Color.white);
        textLabel3.setText("Faster ghosts, average health");
        add(textLabel3);
        add(Box.createVerticalStrut(20));

        GameUIButton hardButton = new GameUIButton("Hardcore");
        add(hardButton);
        add(Box.createVerticalStrut(5));

        JLabel textLabel4 = new JLabel();
        textLabel4.setFont(Config.fontSmall);
        textLabel4.setForeground(Color.white);
        textLabel4.setText("Lightning fast ghosts, only one life");
        add(textLabel4);
        add(Box.createVerticalStrut(20));

        TextButton backButton = new TextButton("Back");
        add(backButton);
        add(Box.createVerticalStrut(20));

        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.game(1);
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.game(2);
            }
        });

        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.game(3);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.toMain();
            }
        });
    }
}
