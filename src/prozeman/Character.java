package prozeman;

public class Character extends Entity {
    private boolean canMove = false;
    private boolean isMoving = false;

    private Direction dir;

    public Character(int size) {
        super(size);
    }

    public void move(Direction direction, int speed) {
        if (isMoving) {
            switch (direction) {
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
            }
        }
    }

    public void stop() {
        isMoving = false;
    }

    public void start() {
        isMoving = true;
    }

    public void setInitialStartingLocation(int startingX, int startingY) {
        if (!canMove) {
            this.x = startingX;
            this.y = startingY;
            canMove = true;
        }
    }
}
