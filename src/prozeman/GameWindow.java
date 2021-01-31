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

        imgPac = Config.pacImage.getScaledInstance(pacman.size, pacman.size, 0);
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

    public void checkEatenPoints() {
        int x = (int) Math.ceil((pacman.getCenterX() - boardRectIncX) / cellSize);
        int y = (int) Math.ceil((pacman.getCenterY() - boardRectIncY) / cellSize);
        if (mapDecoded[y][x].getType() == MapBlock.Type.POINT) {
            mapDecoded[y][x].eatPoint();
        }
    }

    public void checkForWall() {
        //zamiana położenia postaci ze współrzędnych całego frame'a na współrzędne mapy [0,20]
        int x = (int) Math.ceil((pacman.getCenterX() - boardRectIncX) / cellSize);
        int y = (int) Math.ceil((pacman.getCenterY() - boardRectIncY) / cellSize);

        MapBlock block;
        switch (direction) {
            case LEFT:
                block = mapDecoded[y][x - 1];
                break;
            case RIGHT:
                block = mapDecoded[y][x + 1];
                break;
            case UP:
                block = mapDecoded[y - 1][x];
                break;
            case DOWN:
                block = mapDecoded[y + 1][x];
                break;
            default:
                return;
        }
        if (block.getType() == MapBlock.Type.WALL) {
            System.out.printf("%d %d wall, %d %d coords\n", block.getGridX(), block.getGridY(), x, y);
            if (isTouching(pacman, block)) {
                System.out.println("Touching wall");
                direction = Direction.STOP;
                pacman.stop();
                imgPac = Config.pacImage.getScaledInstance(pacman.size, pacman.size, 0);
            }
        }
    }

    public void animation() {
        moveGhost();
        pacman.move(direction, pacSpeed / 10);
        if (isTouching(pacman, ghost)) {
            pacman.stop();
            imgPac = Config.pacImage.getScaledInstance(pacman.size, pacman.size, 0);
        }
        checkEatenPoints();
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

        int cellWidth = cellSize;//boardRect.width / 20;
        int cellHeight = cellSize;//boardRect.height / 20;

        boardRectIncX = boardRect.x;
        boardRectIncY = boardRect.y;

        if (!playing) {
            int x = boardRectIncX;
            int y = boardRectIncY;

            //narysowanie mapy
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    switch (mapDecoded[i][j].getType()) {
                        case WALL:
                            g2d.setColor(Color.blue);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case EMPTY:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case GHOSTSPAWN:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            ghost.setInitialStartingLocation(x, y);
                            break;
                        case PACSPAWN:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            pacman.setInitialStartingLocation(x, y);
                            break;
                        case POINT:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            g2d.setColor(Color.white);
                            g2d.fillOval(x + (cellWidth / 2) - 4, y + (cellHeight / 2) - 4, 8, 8);
                            break;
                    }
                    mapDecoded[i][j].setCoords(x, y);
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
        ghost.start();
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
                        imgPac = Config.pacImageLeft.getScaledInstance(pacman.size, pacman.size, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        direction = Direction.RIGHT;
                        imgPac = Config.pacImageRight.getScaledInstance(pacman.size, pacman.size, 0);
                        break;
                    case KeyEvent.VK_UP:
                        direction = Direction.UP;
                        imgPac = Config.pacImageUp.getScaledInstance(pacman.size, pacman.size, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        direction = Direction.DOWN;
                        imgPac = Config.pacImageDown.getScaledInstance(pacman.size, pacman.size, 0);
                        break;
                    case KeyEvent.VK_CONTROL:
                        direction = Direction.STOP;
                        imgPac = Config.pacImage.getScaledInstance(pacman.size, pacman.size, 0);
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
