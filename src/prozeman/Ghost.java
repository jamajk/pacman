package prozeman;

import java.awt.*;

public class Ghost extends Character {
    private Image image;

    public Ghost(int size) {
        super(size);
        image = Config.ghostImage.getScaledInstance(size, size, 0);
        direction = Direction.STOP;
    }

    @Override
    public void move(int speed) {
        if (isMoving) {
            switch (this.direction) {
                case UP:
                    y -= speed;
                    break;
                case DOWN:
                    y += speed;
                    break;
                case LEFT:
                    x -= speed;
                    break;
                case RIGHT:
                    x += speed;
                    break;
                default:
                    break;
            }
        }
    }
}
