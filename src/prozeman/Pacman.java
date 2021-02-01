package prozeman;

import java.awt.*;

public class Pacman extends Character {
    private Image image;

    public Pacman(int size) {
        super(size);
        image = Config.pacImage.getScaledInstance(size, size, 0);
    }

    @Override
    public void move(int speed) {
        super.move(speed);
        switch (this.direction) {
            case UP:
                image = Config.pacImageUp.getScaledInstance(size, size, 0);
                break;
            case DOWN:
                image = Config.pacImageDown.getScaledInstance(size, size, 0);
                break;
            case LEFT:
                image = Config.pacImageLeft.getScaledInstance(size, size, 0);
                break;
            case RIGHT:
                image = Config.pacImageRight.getScaledInstance(size, size, 0);
                break;
            case STOP:
                image = Config.pacImage.getScaledInstance(size, size, 0);
        }
    }

    public Image getImage() {
        return image;
    }

}
