package prozeman;

public class Character extends Entity {
    protected boolean canMove = false;
    protected boolean isMoving = false;

    protected Direction direction;

    public Character(int size) {
        super(size);
    }

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

    public void resetMovement() {
        canMove = false;
    }

    public void setDirection(Direction newDir) {
        this.direction = newDir;
    }

    public Direction getDirection() {
        return direction;
    }
}
