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
    //private Image[] imgGhost;

    private int ghostSpawnX;
    private int ghostSpawnY;

    private int ghostX_1 = 300;
    private int ghostY_1 = 300;

    private int ghostX_2;
    private int ghostY_2;

    private int ghostX_3;
    private int ghostY_3;

    private int ghostDX;
    private int ghostDY;

    private int pacX = 400;
    private int pacY = 400;

    private int pacDX;
    private int pacDY;


    public GameWindow() {
        try {
            Config.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.darkGray);
        map = Config.maps.get(0);
        //imgGhost = new Image[Config.numberOfGhosts_easy];
        //difficulty easy
    }

    public void animation() {
        moveGhost();
            pacX += pacDX;
            pacY += pacDY;
            ghostX_1 += ghostDX;
            ghostY_1 += ghostDY;
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
        Rectangle boardRect = new Rectangle(40, 40, screenWidth - 80, screenHeight - 80);
        g2d.fill(boardRect);

        int cellWidth = boardRect.width / 20;
        int cellHeight = boardRect.height / 20;

        if (!playing) {
            int x = 40;
            int y = 40;

            //narysowanie mapy
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    switch (map[i][j]) {
                        case 1:
                            g2d.setColor(Color.blue);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case 2:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case 3:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            ghostSpawnX = x;
                            ghostSpawnY = y;
                            break;
                        case 4:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            //pacX = x;
                            //pacY = y;
                            break;
                        case 0:
                            g2d.setColor(Color.black);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            g2d.setColor(Color.white);
                            g2d.fillOval(x + (cellWidth / 2) - 4, y + (cellHeight / 2) - 4, 8, 8);
                            break;

                    }
                    x += cellWidth;
                }
                x = 40;
                y += cellHeight;
            }
            //ghostX_1 = ghostSpawnX;
            //ghostY_1 = ghostSpawnY;
            //ghostX_2 = ghostSpawnX;
            //ghostY_2 = ghostSpawnY;
            //ghostX_3 = ghostSpawnX;
            //ghostY_3 = ghostSpawnY;
        }
        //rysowanie duszków
       /* for (int i = 0; i < Config.numberOfGhosts_easy; i++) {
            imgGhost[i] = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
            g2d.drawImage(imgGhost[i], ghostSpawnX, ghostSpawnY, null);
        }*/
        Image imgGhost = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
        g2d.drawImage(imgGhost, ghostX_1, ghostY_1, null);
        //imgGhost[1] = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
        //g2d.drawImage(imgGhost[1], ghostX_2, ghostY_2, null);
        //imgGhost[2] = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
        //g2d.drawImage(imgGhost[2], ghostX_3, ghostY_3, null);

        //rysowanie pacmana
        Image imgPac = Config.pacImage.getScaledInstance(cellWidth, cellHeight, 0);
        g2d.drawImage(imgPac, pacX, pacY, null);
        g2d.dispose();

        animation();
    }

    private void moveGhost() {
        int move = new Random().nextInt(4) + 1;
        int speed = Config.ghostSpeed_easy;
        switch(move) {
            case 1: //lewo
                ghostDX = -speed;
                ghostDY = 0;
                break;
            case 2: //prawo
                ghostDX = speed;
                ghostDY = 0;
                break;
            case 3: //góra
                ghostDX = 0;
                ghostDY = -speed;
                break;
            case 4: //dół
                ghostDX = 0;
                ghostDY = speed;
                break;
            default:
                break;
        }
    }

    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int speed = Config.playerSpeed_easy / 10;
            int key = e.getKeyCode();

            if (true) {
                if (key == KeyEvent.VK_LEFT) {
                    System.out.println("left");
                    pacDX = -speed;
                    pacDY = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    System.out.println("right");
                    pacDX = speed;
                    pacDY = 0;
                } else if (key == KeyEvent.VK_UP) {
                    System.out.println("up");
                    pacDX = 0;
                    pacDY = -speed;
                } else if (key == KeyEvent.VK_DOWN) {
                    System.out.println("down");
                    pacDX = 0;
                    pacDY = speed;
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
