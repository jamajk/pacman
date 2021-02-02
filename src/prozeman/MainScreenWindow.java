package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;

public class MainScreenWindow extends JPanel {
    public MainScreenWindow(pWindow parent) {

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
        GameUIButton newGameButton = new GameUIButton("New Game");
        add(newGameButton);
        add(Box.createVerticalStrut(20));

        GameUIButton highScoreButton = new GameUIButton("High Scores");
        add(highScoreButton);
        add(Box.createVerticalStrut(20));

        GameUIButton howToPlayButton = new GameUIButton("How to play");
        add(howToPlayButton);
        add(Box.createVerticalStrut(20));

        GameUIButton exitButton = new GameUIButton("Exit");
        add(exitButton);
        add(Box.createVerticalStrut(20));


        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("New Game");
                //parent.layout.next(parent.content);//layout.show(parent.content, "gameWindow");
                //parent.game();
                parent.diffSelect();
            }
        });

        highScoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("High scores");
            }
        });

        howToPlayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("How to play");
                parent.howToPlay();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

}
