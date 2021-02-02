package prozeman;

public class MapBlock extends Entity {
    private int mapSize = 20;

    private Type type;
    private int gridX;
    private int gridY;

    enum Type {
        EMPTY, WALL, POINT, GHOSTSPAWN, PACSPAWN, BONUS;
    }

    public MapBlock(int size, int type, int i, int j) {
        super(size);
        switch (type) {
            case 0:
                this.type = Type.POINT;
                break;
            case 1:
                this.type = Type.WALL;
                break;
            case 2:
                this.type = Type.EMPTY;
                break;
            case 3:
                this.type = Type.GHOSTSPAWN;
                break;
            case 4:
                this.type = Type.PACSPAWN;
                break;
            case 5:
                this.type = Type.BONUS;
                break;
            default:
                this.type = Type.EMPTY;
                break;
        }
        gridX = j;
        gridY = i;
    }

    public int getGridX() { return gridX; }

    public int getGridY() {
        return gridY;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapBlock.Type getType() {
        return type;
    }

    public void eatPoint() {
        if (type == Type.POINT || type == Type.BONUS) {
            type = Type.EMPTY;
        }
    }
}
