package prozeman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class GameWindow extends JPanel implements ActionListener {
    private boolean playing = false;
    private int[][] map;
    private MapBlock[][] mapDecoded;
    private int cellSize = 30;
    private int boardRectIncX;
    private int boardRectIncY;

    private Character pacman;
    private Character ghost;

    private int pacSpeed;
    private Direction direction = Direction.STOP;

    private Image imgPac;

    public GameWindow() {
        try {
            Config.loadConfig();
            pacSpeed = Config.playerSpeed_easy;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.darkGray);
        map = Config.maps.get(0);
        decodeMap();
        pacman = new Character(cellSize);//new Pacman(cellSize);
        ghost = new Character(cellSize);

        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
        //difficulty easy
    }

    public void decodeMap() {
        mapDecoded = new MapBlock[20][20];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                mapDecoded[i][j] = new MapBlock(30, map[i][j], i, j);
            }
        }
    }

    public void checkForEatenPoints() {
        int y = (int) Math.ceil((pacman.getCenterX() / cellSize) - (boardRectIncX / cellSize));
        int x = (int) Math.ceil((pacman.getCenterY() / cellSize) - (boardRectIncY / cellSize));
        if (mapDecoded[x][y].getType() == MapBlock.Type.POINT) {
            mapDecoded[x][y].eatPoint();
        }
    }

    public void checkForWall() {
        int y = (int) Math.ceil((pacman.getCenterX() / cellSize) - (boardRectIncX / cellSize));
        int x = (int) Math.ceil((pacman.getCenterY() / cellSize) - (boardRectIncY / cellSize));
        MapBlock block;
        switch (direction) {
            case LEFT:
                block = mapDecoded[x - 1][y];
                if (block.getType() == MapBlock.Type.WALL) {
                    System.out.printf("%d, %d wall, %d %d coords, wall to the left\n", x - 1, y, x, y);
                    if (isTouching(pacman, block)) {
                        direction = Direction.STOP;
                        pacman.stop();
                        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
                    }
                    pacman.stop();
                }
                break;
            case RIGHT:
                block = mapDecoded[x + 1][y];
                if (block.getType() == MapBlock.Type.WALL) {
                    System.out.printf("%d, %d wall, %d %d coords, wall to the right\n", x + 1, y, x, y);
                    if (isTouching(pacman, block)) {
                        direction = Direction.STOP;
                        pacman.stop();
                        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
                    }
                    pacman.stop();
                }
                break;
            case UP:
                block = mapDecoded[x][y - 1];
                if (block.getType() == MapBlock.Type.WALL) {
                    System.out.printf("%d, %d wall, %d %d coords, wall up\n", x, y - 1, x, y);
                    if (isTouching(pacman, block)) {
                        direction = Direction.STOP;
                        pacman.stop();
                        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
                    }
                    pacman.stop();
                }
                break;
            case DOWN:
                block = mapDecoded[x][y + 1];
                if (block.getType() == MapBlock.Type.WALL) {
                    System.out.printf("%d, %d wall, %d %d coords, wall down\n", x, y + 1, x, y);
                    if (isTouching(pacman, block)) {
                        direction = Direction.STOP;
                        pacman.stop();
                        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
                    }
                    pacman.stop();
                }
                break;
            case STOP:
                break;
            default:
                break;
        }
    }

    public void animation() {
        moveGhost();
        pacman.move(direction, pacSpeed / 10);
        if (isTouching(pacman, ghost)) {
            pacman.stop();
            imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
        }
        checkForEatenPoints();
        checkForWall();
        repaint();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        int screenHeight = this.getBounds().height;
        int screenWidth = this.getBounds().width;
        Rectangle boardRect = new Rectangle((screenWidth / 2) - 300, (screenHeight / 2) - 300, 600, 600);
        g2d.fill(boardRect);

        int cellWidth = boardRect.width / 20;
        int cellHeight = boardRect.height / 20;

        boardRectIncX = boardRect.x;
        boardRectIncY = boardRect.y;

        if (!playing) {
            int x = boardRect.x;
            int y = boardRect.y;

            //narysowanie mapy
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    switch (mapDecoded[i][j].getType()) {
                        case WALL:
                            g2d.setColor(Color.blue);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            mapDecoded[i][j].setCoords(x + 15, y + 15);
                            break;
                        case EMPTY:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            mapDecoded[i][j].setCoords(x + 15, y + 15);
                            break;
                        case GHOSTSPAWN:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            ghost.setInitialStartingLocation(x, y);
                            mapDecoded[i][j].setCoords(x + 15, y + 15);
                            break;
                        case PACSPAWN:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            pacman.setInitialStartingLocation(x, y);
                            mapDecoded[i][j].setCoords(x + 15, y + 15);
                            break;
                        case POINT:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            g2d.setColor(Color.white);
                            g2d.fillOval(x + (cellWidth / 2) - 4, y + (cellHeight / 2) - 4, 8, 8);
                            mapDecoded[i][j].setCoords(x + 15, y + 15);
                            break;
                    }
                    x += cellWidth;
                }
                x = boardRect.x;
                y += cellHeight;
            }
        }
        //rysowanie duszków
       /* for (int i = 0; i < Config.numberOfGhosts_easy; i++) {
            imgGhost[i] = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
            g2d.drawImage(imgGhost[i], ghostSpawnX, ghostSpawnY, null);
        }*/
        Image imgGhost = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
        g2d.drawImage(imgGhost, ghost.getX(), ghost.getY(), null);


        //rysowanie pacmana
        g2d.drawImage(imgPac, pacman.getX(), pacman.getY(), null);
        g2d.dispose();

        animation();
    }

    private void moveGhost() {
        int move = new Random().nextInt(4) + 1;
        int speed = Config.ghostSpeed_easy;
        switch(move) {
            case 1: //lewo
                ghost.move(Direction.LEFT, speed);
                break;
            case 2: //prawo
                ghost.move(Direction.RIGHT, speed);
                break;
            case 3: //góra
                ghost.move(Direction.UP, speed);
                break;
            case 4: //dół
                ghost.move(Direction.DOWN, speed);
                break;
            default:
                break;
        }
    }

    private boolean isTouching(Entity first, Entity second) {
        if (first.getX() <= second.getRightX() && first.getRightX() >= second.getX() &&
                first.getDownY() >= second.getY() && first.getY() <= second.getDownY()) {
            System.out.println("touching");
            return true;
        }
        return false;
    }

    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (true) {
                pacman.start();
                switch(key) {
                    case KeyEvent.VK_LEFT:
                        direction = Direction.LEFT;
                        imgPac = Config.pacImageLeft.getScaledInstance(cellSize, cellSize, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        direction = Direction.RIGHT;
                        imgPac = Config.pacImageRight.getScaledInstance(cellSize, cellSize, 0);
                        break;
                    case KeyEvent.VK_UP:
                        direction = Direction.UP;
                        imgPac = Config.pacImageUp.getScaledInstance(cellSize, cellSize, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        direction = Direction.DOWN;
                        imgPac = Config.pacImageDown.getScaledInstance(cellSize, cellSize, 0);
                        break;
                    case KeyEvent.VK_CONTROL:
                        direction = Direction.STOP;
                        imgPac = Config.pacImage.getScaledInstance(cellSize, cellSize, 0);
                        break;
                    default:
                        break;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    playing = true;
                }
            }
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
