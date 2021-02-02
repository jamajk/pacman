package prozeman;

import javax.swing.*;
import java.awt.*;


public class pWindow extends JFrame {
    CardLayout layout = new CardLayout();
    JPanel content = new JPanel(layout);

    public pWindow() {
        super("PROZEman");

        MainScreenWindow main = new MainScreenWindow(this);

        GameWindow game = new GameWindow();
        game.setFocusable(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setMinimumSize(new Dimension(800, 800));
        setLocationRelativeTo(null);

        //content.add(main, "Main Menu");
        content.add(game, "Game Screen");
        add(content);

        setVisible(true);
    }

    public void game() {
        layout.show(content, "Game Screen");
    }

}
