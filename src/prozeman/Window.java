package prozeman;

import javax.swing.*;
import java.awt.*;


public class Window extends JFrame {
    //CardLayout layout = new CardLayout();
    //Container contentPane = getContentPane();
    public Window() {
        super("PROZEman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        //setLayout(layout);
        add(new MainScreenWindow());
        add(new GameWindow());
        //layout.first(contentPane);
        setVisible(true);
        setMinimumSize(new Dimension(800, 600));


        getContentPane().setBackground(Color.darkGray);
    }


}
