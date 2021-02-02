package prozeman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLostScreen extends JPanel {
    public GameLostScreen(pWindow parent, int diff) {
        setBackground(Color.darkGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel textLabel1 = new JLabel();
        textLabel1.setFont(Config.fontLarge);
        textLabel1.setForeground(Color.yellow);
        textLabel1.setText("Game Over!");
        add(textLabel1);
        add(Box.createVerticalStrut(80));

        GameUIButton restartButton = new GameUIButton("Restart Game");
        add(restartButton);
        add(Box.createVerticalStrut(30));

        GameUIButton mainMenuButton = new GameUIButton("Go to Main Menu");
        add(mainMenuButton);
        add(Box.createVerticalStrut(30));

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.game(diff);
            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.toMain();
            }
        });
    }
}
