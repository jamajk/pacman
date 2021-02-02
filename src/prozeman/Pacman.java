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
                image = Config.pacImageUp;
                break;
            case DOWN:
                image = Config.pacImageDown;
                break;
            case LEFT:
                image = Config.pacImageLeft;
                break;
            case RIGHT:
                image = Config.pacImageRight;
                break;
            case STOP:
                image = Config.pacImage;
        }
    }

    public Image getImage() {
        return image;
    }

}
