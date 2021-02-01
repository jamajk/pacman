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

    private Pacman pacman;
    private Ghost[] ghosts;

    private int pacSpeed;

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
        pacman = new Pacman(cellSize);
        ghosts = new Ghost[Config.numberOfGhosts_easy];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(cellSize);
        }

        pacman.setDirection(Direction.STOP);
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

    public void checkForWall(Character walker) {
        //zamiana położenia postaci ze współrzędnych całego frame'a na współrzędne mapy [0,20]
        int x = (int) Math.ceil((walker.getCenterX() - boardRectIncX) / cellSize);
        int y = (int) Math.ceil((walker.getCenterY() - boardRectIncY) / cellSize);

        MapBlock block;
        switch (walker.getDirection()) {
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
            if (isTouching(walker, block)) {
                System.out.println("Touching wall");
                walker.setDirection(Direction.STOP);
                walker.stop();
            }
        }
    }

    public void animation() {
        for (int i = 0; i < ghosts.length; i++) {
            moveGhost(ghosts[i]);
            checkForWall(ghosts[i]);
            if (isTouching(pacman, ghosts[i])) {
                pacman.stop();
            }
        }
        pacman.move(pacSpeed / 10);
        checkEatenPoints();
        checkForWall(pacman);

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
                            for (int k = 0; k < ghosts.length; k++) {
                                ghosts[k].setInitialStartingLocation(x, y);
                            }
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
        for (int i = 0; i < ghosts.length; i++) {
            Image imgGhost = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
            g2d.drawImage(imgGhost, ghosts[i].getX(), ghosts[i].getY(), null);
        }

        //rysowanie pacmana
        g2d.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), null);
        g2d.dispose();

        animation();
    }

    private void moveGhost(Ghost ghost) {
        int move = new Random().nextInt(4) + 1;
        int speed = Config.ghostSpeed_easy / 5;
        if (ghost.getDirection() == Direction.STOP) {
            ghost.start();
            switch(move) {
                case 1: //lewo
                    ghost.setDirection(Direction.LEFT);
                    break;
                case 2: //prawo
                    ghost.setDirection(Direction.RIGHT);
                    break;
                case 3: //góra
                    ghost.setDirection(Direction.UP);
                    break;
                case 4: //dół
                    ghost.setDirection(Direction.DOWN);
                    break;
                default:
                    break;
            }
        }
        ghost.move(speed);

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
                        pacman.setDirection(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        pacman.setDirection(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_UP:
                        pacman.setDirection(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        pacman.setDirection(Direction.DOWN);
                        break;
                    case KeyEvent.VK_CONTROL:
                        pacman.setDirection(Direction.STOP);
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
