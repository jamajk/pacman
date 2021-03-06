package prozeman;

import javax.swing.*;
import java.awt.*;

public class GameUIButton extends JButton {
    public GameUIButton(String text) {
        super(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMinimumSize(new Dimension(150, 40));
        setMaximumSize(new Dimension(400, 70));

        setBackground(Color.lightGray);
        setForeground(Color.white);
        setFont(Config.fontLarge);
        setBorderPainted(false);
    }
}
