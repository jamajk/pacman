package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class GameWonScreen extends JPanel {
    public GameWonScreen(pWindow parent, int score) {

        setBackground(Color.darkGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel textLabel1 = new JLabel();
        textLabel1.setFont(Config.fontLarge);
        textLabel1.setForeground(Color.yellow);
        textLabel1.setText("You Won!");
        add(textLabel1);
        add(Box.createVerticalStrut(60));

        JLabel textLabel2 = new JLabel();
        textLabel2.setFont(Config.fontLarge);
        textLabel2.setForeground(Color.white);
        textLabel2.setText("<html>Your score:<br>" + Integer.toString(score) + "</html>");
        add(textLabel2);
        add(Box.createVerticalStrut(60));

        JLabel textLabel3 = new JLabel();
        textLabel3.setFont(Config.fontLarge);
        textLabel3.setForeground(Color.white);
        textLabel3.setText("Enter your name:");
        add(textLabel3);
        add(Box.createVerticalStrut(30));

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(150, 40));
        add(nameField);
        add(Box.createVerticalStrut(60));

        GameUIButton confirmButton = new GameUIButton("Confirm");
        add(confirmButton);
        add(Box.createVerticalStrut(60));

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //zapisanie high score
                if (nameField.getText().isEmpty() == false) {
                    System.out.println(nameField.getText());
                    System.out.println(score);
                    parent.toMain();
                }
            }
        });
    }
}
