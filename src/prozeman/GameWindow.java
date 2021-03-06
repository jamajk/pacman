package prozeman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Klasa wyświetlająca plansze i umożliwiająca rozgrywkę.
 */
public class GameWindow extends JPanel {
    private int difficulty;
    private boolean playing;
    private boolean bonus;
    private int lives;
    private int[][] map;
    private MapBlock[][] mapDecoded;
    private int cellSize = 30;
    private int boardRectIncX;
    private int boardRectIncY;
    private pWindow parent;

    private Pacman pacman;
    private Ghost[] ghosts;

    private int pellets;
    private int level;

    private int pacSpeed;
    private int ghostSpeed;

    private long startTime; //nanoseconds
    private long prevTime;
    private long elapsedTime; //nanoseconds

    private int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    private int finalScore;

    /**
     * Konstruktor klasy GameWindow
     */
    public GameWindow(pWindow parent, int difficulty) {
        this.parent = parent;
        this.difficulty = difficulty;
        setFocusable(true);
        requestFocusInWindow();
        elapsedTime = 0;
        prevTime = 0;
        pellets = 0;
        level = 0;
        playing = false;

        try {
            Config.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        setBackground(Color.darkGray);
        map = Config.maps.get(level);
        decodeMap();
        pacman = new Pacman(cellSize);
        pacman.setDirection(Direction.STOP);
        loadDifficulty();

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), Direction.UP);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), Direction.DOWN);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), Direction.LEFT);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), Direction.RIGHT);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("control CONTROL"), Direction.STOP);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), "START");
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("P"), "PAUSE");
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("ESCAPE"), "PAUSE");


        this.getActionMap().put(Direction.UP, new MoveAction(Direction.UP));
        this.getActionMap().put(Direction.DOWN, new MoveAction(Direction.DOWN));
        this.getActionMap().put(Direction.LEFT, new MoveAction(Direction.LEFT));
        this.getActionMap().put(Direction.RIGHT, new MoveAction(Direction.RIGHT));
        this.getActionMap().put(Direction.STOP, new MoveAction(Direction.STOP));
        this.getActionMap().put("START", new MiscAction("START"));
        this.getActionMap().put("PAUSE", new MiscAction("PAUSE"));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                animation();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Metoda wczytująca odpowiednie liczby związane z poziomem trudności oraz tworząca odpowiednią liczbę duszków
     */
    private void loadDifficulty() {
        int nGhosts;
        switch (difficulty) {
            case 1: //easy
                pacSpeed = Config.playerSpeed_easy;
                ghostSpeed = Config.ghostSpeed_easy;
                lives = Config.numberOfLives_easy;
                nGhosts = Config.numberOfGhosts_easy;
                break;
            case 2: //medium
                pacSpeed = Config.playerSpeed_medium;
                ghostSpeed = Config.ghostSpeed_medium;
                lives = Config.numberOfLives_medium;
                nGhosts = Config.numberOfGhosts_medium;
                break;
            case 3: //hard
                pacSpeed = Config.playerSpeed_hardcore;
                ghostSpeed = Config.ghostSpeed_hardcore;
                lives = Config.numberOfLives_hardcore;
                nGhosts = Config.numberOfGhosts_hardcore;
                break;
            default: //to nie powinno sie zdarzyć
                pacSpeed = Config.playerSpeed_easy;
                ghostSpeed = Config.ghostSpeed_easy;
                lives = Config.numberOfLives_easy;
                nGhosts = Config.numberOfGhosts_easy;
        }
        ghosts = new Ghost[nGhosts];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(cellSize);
        }
    }

    /**
     * Metoda zamieniająca mapę jako tablicę liczb int na tablicę obiektów MapBlock, które mają rozróżnialne typy
     */
    public void decodeMap() {
        mapDecoded = new MapBlock[20][20];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                mapDecoded[i][j] = new MapBlock(30, map[i][j], i, j);
            }
        }
    }

    /**
     * Metoda sprawdzająca, czy w miejscu w którym znajduje się pacman jest kulka do zjedzenia lub bonus.
     * Jeśli gracz napotka na bonus, na okres pęciu sekund zwiększona zostanie jego szybkość oraz zapewniona będzie nieśmiertelność
     */
    public void checkEatenPoints() {
        int x = (int) Math.ceil((pacman.getCenterX() - boardRectIncX) / cellSize);
        int y = (int) Math.ceil((pacman.getCenterY() - boardRectIncY) / cellSize);
        if (mapDecoded[y][x].getType() == MapBlock.Type.POINT) {
            mapDecoded[y][x].eatPoint();
        } else if (mapDecoded[y][x].getType() == MapBlock.Type.BONUS) {
            int oldSpeed = pacSpeed;
            pacSpeed *= 2;//1.5;
            bonus = true;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    pacSpeed = oldSpeed;
                    bonus = false;
                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.schedule(runnable, 10, TimeUnit.SECONDS);
            mapDecoded[y][x].eatPoint();
        }
    }

    /**
     * Metoda obsługująca kolizje postaci z otoczeniem - sprawdzająca czy w kierunku, w którym porusza się postać czeka na nią zderzenie ze ścianą
     * @param walker - obiekt klasy Character (pacman lub duszek) dla którego ma zostać sprawodzona kolizja
     */
    public void checkForWall(Character walker) {
        //zamiana położenia postaci ze współrzędnych całego frame'a na współrzędne mapy [0,20]
        int x = (int) Math.ceil((walker.getCenterX() - boardRectIncX) / cellSize);
        int y = (int) Math.ceil((walker.getCenterY() - boardRectIncY) / cellSize);

        if (x == 0 || y == 0 || x == 19 || y == 19) {
            walker.resetMovement();
            return;
        }

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
            if (isTouching(walker, block)) {
                walker.setDirection(Direction.STOP);
                walker.stop();
            }
        }
    }

    /**
     * Metoda wprawiająca w ruch obiekty na ekranie
     */
    public void animation() {
        if (playing) {
            for (int i = 0; i < ghosts.length; i++) {
                moveGhost(ghosts[i]);
                checkForWall(ghosts[i]);
                if (isTouching(pacman, ghosts[i])) {
                    if (!bonus) {
                        pacman.stop();
                        loseLife();
                    }
                }
                if (pellets <= 0) {
                    playing = false;
                    pause();
                    passedLevel();
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
    }

    /**
     * Metoda kontrolująca przechodzenie poszczególnych poziomów rozgrywki.
     * Gdy gracz ukończy ostatnią planszę, gra zostanie zakończona a tu zostanie obliczony jego wynik
     */
    private void passedLevel() {
        pellets = 1;
        if (level >= Config.numberOfLevels - 1) {
            int n_d;
            switch (difficulty) {
                case 1:
                    n_d = 10000;
                    break;
                case 2:
                    n_d = 30000;
                    break;
                case 3:
                    n_d = 100000;
                    break;
                default:
                    n_d = 1000;
                    break;
            }
            finalScore = (lives * n_d * Config.numberOfLevels) / (int) elapsedTime;
            parent.gameWon(finalScore);
        } else if (!playing) {
            level++;
            map = Config.maps.get(level);
            decodeMap();
            pacman.resetMovement();
            for (Ghost ghost : ghosts) {
                ghost.resetMovement();
            }
        }
    }

    /**
     * Metoda kontrolująca ubywające życia po zderzeniu z duszkami.
     * Gdy życia kończą się, gra zostaje przerwana
     */
    private void loseLife() {
        lives--;
        if (lives <= 0) {
            parent.gameLost(difficulty);
            return;
        }
        pacman.resetMovement();
        for (Ghost ghost : ghosts) {
            ghost.resetMovement();
        }
        pause();
    }

    /**
     * Metoda zatrzymująca czas gry
     */
    private void pause() {
        prevTime = elapsedTime;
        pacman.stop();
        pacman.setDirection(Direction.STOP);
        for (Ghost ghost : ghosts) {
            ghost.stop();
            ghost.setDirection(Direction.STOP);
        }
        playing = false;
    }

    /**
     * Metoda pozwalająca wyświetlić wiadomość startową na ekranie
     * @param g2d - kontekst graficzny
     * @param message - treść wiadomości
     */
    private void showMessage(Graphics2D g2d, String message) {

        g2d.setFont(Config.fontLarge);
        g2d.setColor(Color.darkGray);
        g2d.fillRect(boardRectIncX + 150, boardRectIncY + 200, 300, 100);
        g2d.setColor(Color.yellow);
        g2d.drawString(message, boardRectIncX + 170, boardRectIncY + 260);
    }

    /**
     * Metoda paintComponent(), tu odbywa się rysowanie graficznych elementów na ekranie
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        int screenHeight = this.getBounds().height;
        int screenWidth = this.getBounds().width;
        Rectangle boardRect = new Rectangle((screenWidth / 2) - 300, (screenHeight / 2) - 300, 600, 600);
        g2d.fill(boardRect);

        int cellWidth = cellSize;
        int cellHeight = cellSize;

        boardRectIncX = boardRect.x;
        boardRectIncY = boardRect.y;
        int x = boardRectIncX;
        int y = boardRectIncY;

        //narysowanie mapy
        int pts = 0;
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
                        pts += 1;
                        g2d.setColor(Color.black);
                        g2d.fillRect(x, y, cellWidth, cellHeight);
                        g2d.setColor(Color.white);
                        g2d.fillOval(x + (cellWidth / 2) - 4, y + (cellHeight / 2) - 4, 8, 8);
                        break;
                    case BONUS:
                        pts += 1;
                        g2d.setColor(Color.black);
                        g2d.fillRect(x, y, cellWidth, cellHeight);
                        g2d.setColor(Color.red);
                        g2d.fillOval(x + (cellWidth / 2) - 7, y + (cellHeight / 2) - 7, 14, 14);
                        break;
                }
                mapDecoded[i][j].setCoords(x, y);
                x += cellWidth;
            }
            x = boardRect.x;
            y += cellHeight;
        }
        pellets = pts;

        //rysowanie duszków
        for (int i = 0; i < ghosts.length; i++) {
            Image imgGhost = Config.ghostImage.getScaledInstance(cellWidth, cellHeight, 0);
            g2d.drawImage(imgGhost, ghosts[i].getX(), ghosts[i].getY(), null);
        }

        //rysowanie pacmana
        g2d.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), null);

        //narysowanie wiadomości startowej
        if (!playing) {
            showMessage(g2d, "Press SPACE to play");
        }

        //rysowanie serc
        int hx = screenWidth - 60;
        int hy = boardRectIncY - 45;
        Image image = Config.heartImage.getScaledInstance(30, 30, 0);
        for (int i = 0; i < lives; i++) {
            g2d.drawImage(image, hx, hy, this);
            hx -= 35;
        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(Config.fontLarge);
        g2d.drawString("Lives:", hx - 50, hy + 25);

        //rysowanie czasu gry
        if (playing) {
            elapsedTime = prevTime + (System.nanoTime() - startTime) / 1_000_000_000;
        }
        String time = Integer.toString(Math.toIntExact(elapsedTime));
        g2d.drawString("Time: " + time + "s", 60, hy + 25);

        //rysowanie numeru poziomu
        g2d.drawString("Level " + Integer.toString(level + 1), (screenWidth / 2) - 50, hy + 25);

        //rysowanie wskaźnika bonusu
        if (bonus) {
            g2d.setColor(Color.red);
            g2d.fillOval(30, hy + 11, 14, 14);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    /**
     * Metoda wybierająca kierunek kolejnego ruchu duszka po zderzeniu ze ścianą
     * @param ghost - wybiera, którego duszka na ekranie ma dotyczyć ruch
     */
    private void moveGhost(Ghost ghost) {
        int move = new Random().nextInt(4) + 1;
        int speed = ghostSpeed / 5;
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

    /**
     * Metoda pomocnicza sprawdzająca czy dwa obiekty dotykają się, badając ich współrzędne na ekranie.
     * @param first - obiekt numer 1
     * @param second - obiekt numer 2
     * @return wartość typu Boolean mówiąca czy obiekty przekazane jako paramtery dotykają się
     */
    private boolean isTouching(Entity first, Entity second) {
        if (first.getX() <= second.getRightX() && first.getRightX() >= second.getX() &&
                first.getDownY() >= second.getY() && first.getY() <= second.getDownY()) {
            return true;
        }
        return false;
    }

    /**
     * Klasa obsługująca akcje związane z ruchem Pacmana
     */
    private class MoveAction extends AbstractAction {

        Direction direction;

        MoveAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) { ;
            int key = e.getID();
            if (playing) {
                pacman.start();
                pacman.setDirection(direction);
            }
            repaint();
        }
    }

    /**
     * Klasa obsługująca pozostałe akcje wywołane przez gracza
     */
    private class MiscAction extends AbstractAction {

        String action;

        MiscAction(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) { ;
            switch (action) {
                case "START":
                    if (!playing) {
                        playing = true;
                        startTime = System.nanoTime();
                    }
                    break;
                case "PAUSE":
                    pause();
                    break;
                default:
                    break;
            }
            repaint();
        }
    }
}
