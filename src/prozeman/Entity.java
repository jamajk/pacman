package prozeman;

public class Entity {
    protected int size;
    protected int x;
    protected int y;

    Entity(int size) {
        this.size = size;
    }

    Entity(int size, int x, int y) {
        this(size);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCenterX() {
        return x + size / 2;
    }

    public int getCenterY() {
        return y + size / 2;
    }

    public int getRightX() {
        return x + size;
    }

    public int getDownY() {
        return y + size;
    }
}
