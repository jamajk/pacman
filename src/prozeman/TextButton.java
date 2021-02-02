package prozeman;

import javax.swing.*;
import java.awt.*;

public class TextButton extends JButton {
    public TextButton(String text) {
        super(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMinimumSize(new Dimension(150, 40));
        setMaximumSize(new Dimension(400, 70));

        setBackground(Color.darkGray);
        setForeground(Color.white);
        setFont(Config.fontLarge);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }
}
