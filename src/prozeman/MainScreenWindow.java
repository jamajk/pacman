package prozeman;

import javax.swing.*;
import java.awt.*;

public class MainScreenWindow extends JFrame {
    public MainScreenWindow() {
        super("PROZEman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(Color.darkGray);
    }
}
