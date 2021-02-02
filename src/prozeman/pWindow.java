package prozeman;

import javax.swing.*;
import java.awt.*;


public class pWindow extends JFrame {
    CardLayout layout = new CardLayout();
    JPanel content = new JPanel(layout);

    public pWindow() {
        super("PROZEman");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setMinimumSize(new Dimension(800, 800));
        setLocationRelativeTo(null);

        content.add(new MainScreenWindow(this), "Main Menu");
        content.add(new HowToPlayScreen(this), "How To Play");
        content.add(new DiffSelectScreen(this), "Select");
        add(content);

        setVisible(true);
    }

    public void game(int dif) {
        content.add(new GameWindow(this, dif), "Game Screen");
        layout.show(content, "Game Screen");
    }

    public void diffSelect() { layout.show(content, "Select"); }

    public void howToPlay() { layout.show(content, "How To Play"); }

    public void toMain() { layout.show(content, "Main Menu"); }

}
