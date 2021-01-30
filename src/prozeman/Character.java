package prozeman;

public class Character extends Entity {
    private boolean canMove = false;
    private boolean isMoving = false;

    private int dx;
    private int dy;

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
        System.out.printf("x: %d y: %d\n", getCenterX(), getCenterY());

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

    public int[] getGridPositionOnMap() {
        int blockSize = 30;
        int i = (int) Math.ceil(getCenterX() / blockSize);
        int j = (int) Math.ceil(getCenterY() / blockSize);
        System.out.printf("x: %d, y: %d \ngx: %d, gy: %d", getCenterX(), getCenterY(), i, j);
        int[] coords = new int[2];
        coords[0] = i;
        coords[1] = j;
        return coords;
    }
}
