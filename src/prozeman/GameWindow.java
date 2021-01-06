package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class GameWindow extends JPanel implements ActionListener {
    public GameWindow() {
        System.out.println("es");
        setBackground(Color.yellow);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
