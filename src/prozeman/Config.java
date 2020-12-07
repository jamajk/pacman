package prozeman;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Klasa przechowywująca oraz wczytująca parametry rozgrywki możliwe do własnoręcznego konfigurowania.
 * Wczytuje ona również informacje o poziomach wykorzystywanych w rozgrywce.
 * <p>
 *     Odpowiednie zmienne są wczytywane z pliku config.txt znajdującego się w plikach gry.
 * </p>
 */
public class Config {
    /**
     * Zmienna opisująca prędkość gracza (Pacmana) podczas rozgrywki na łatwym poziomie trudności
     */
    static int playerSpeed_easy;

    /**
     * Zmienna opisująca prędkość gracza (Pacmana) podczas rozgrywki na średnim poziomie trudności
     */
    static int playerSpeed_medium;

    /**
     * Zmienna opisująca prędkość gracza (Pacmana) podczas rozgrywki na trudnym poziomie trudności
     */
    static int playerSpeed_hardcore;

    /**
     * Zmienna opisująca prędkość duszków podczas rozgrywki na łatwym poziomie trudności
     */
    static int ghostSpeed_easy;

    /**
     * Zmienna opisująca prędkość duszków podczas rozgrywki na średnim poziomie trudności
     */
    static int ghostSpeed_medium;

    /**
     * Zmienna opisująca prędkość duszków podczas rozgrywki na trudnym poziomie trudności
     */
    static int ghostSpeed_hardcore;

    /**
     * Zmienna opisująca ilość żyć podczas rozgrywki na łatwym poziomie trudności
     */
    static int numberOfHearts_easy;

    /**
     * Zmienna opisująca ilość żyć podczas rozgrywki na średnim poziomie trudności
     */
    static int numberOfHearts_medium;

    /**
     * Zmienna opisująca ilość żyć podczas rozgrywki na trudnym poziomie trudności
     */
    static int numberOfHearts_hardcore;

    /**
     * Zmienna opisująca liczbę duszków podczas rozgrywki na łatwym poziomie trudności
     */
    static int numberOfGhosts_easy;

    /**
     * Zmienna opisująca liczbę duszków podczas rozgrywki na średnim poziomie trudności
     */
    static int numberOfGhosts_medium;

    /**
     * Zmienna opisująca liczbę duszków podczas rozgrywki na trudnym poziomie trudności
     */
    static int numberOfGhosts_hardcore;

    /**
     * Zmienna opisująca liczbę poziomów do ukończenia podczas rozgrywki
     */
    static int numberOfLevels;

    /**
     * Lista poziomów o długości specyfikowanej przez zmienną numberOfLevels, przechowująca informację o wyglądzie planszy
     * w postaci dwuwymiarowych tablic liczb typu int
     */
    static ArrayList<int[][]> maps;

    /**
     * Metoda pozwalająca wczytać do programu zmienne konfigurowalne w pliku "config.txt"
     */
    static void loadConfig() throws IOException {
        InputStream file = new FileInputStream("src/configs/config1.txt");
        Properties config = new Properties();
        config.load(file);
        playerSpeed_easy=Integer.parseInt(config.getProperty("playerspeed-e"));
        playerSpeed_medium=Integer.parseInt(config.getProperty("playerspeed-m"));
        playerSpeed_hardcore=Integer.parseInt(config.getProperty("playerspeed-h"));
        ghostSpeed_easy=Integer.parseInt(config.getProperty("ghostspeed-e"));
        ghostSpeed_medium=Integer.parseInt(config.getProperty("ghostspeed-m"));
        ghostSpeed_hardcore=Integer.parseInt(config.getProperty("ghostspeed-h"));
        numberOfHearts_easy=Integer.parseInt(config.getProperty("nohearts-e"));
        numberOfHearts_medium=Integer.parseInt(config.getProperty("nohearts-m"));
        numberOfHearts_hardcore=Integer.parseInt(config.getProperty("nohearts-h"));
        numberOfHearts_easy=Integer.parseInt(config.getProperty("noghosts-e"));
        numberOfHearts_medium=Integer.parseInt(config.getProperty("noghosts-m"));
        numberOfHearts_hardcore=Integer.parseInt(config.getProperty("noghosts-h"));

        numberOfLevels=Integer.parseInt(config.getProperty("nolevels"));

        file.close();

        maps = new ArrayList<>();
        for (int n = 0; n < numberOfLevels; n++) {
            loadMap(n);
        }
    }
    /**
     * Metoda pozwalająca wczytać do programu informacje o pojedynczej planszy z pliku "map%.txt",
     * gdzie % oznacza numer wczytywanego poziomu
     */
    private static void loadMap(int mapNumber) {
        int[][] tempMap = new int[10][20];
        mapNumber += 1;
        String pathName = "src/configs/map" + mapNumber + ".txt";
        File file = new File(pathName);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                for (int i = 0; i < tempMap.length; i++) {
                    for (int j = 0; j < tempMap[i].length; j++) {
                        tempMap[i][j] = Integer.parseInt(scanner.next());
                    }
                }
            }
            scanner.close();
            maps.add(tempMap);
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathName);
        }
    }
    /**
     * Metoda debugowa służąca do wyświetlenia wczytanych danych w celu sprawdzenia ich poprawności
     */
    static void printLoadedConfig() {
        System.out.println(playerSpeed_easy);
        System.out.println(playerSpeed_medium);
        System.out.println(playerSpeed_hardcore);

        System.out.println(ghostSpeed_easy);
        System.out.println(ghostSpeed_medium);
        System.out.println(ghostSpeed_hardcore);

        System.out.println(numberOfHearts_easy);
        System.out.println(numberOfHearts_medium);
        System.out.println(numberOfHearts_hardcore);

        System.out.println(numberOfGhosts_easy);
        System.out.println(numberOfGhosts_medium);
        System.out.println(numberOfGhosts_hardcore);

    }

    /**
     * Metoda debugowa służąca do wyświetlenia wczytanych informacji o planszach w celu sprawdzenia ich poprawności
     */
    static void printMaps() {
        for (int num = 0; num < maps.size(); num++) {
            System.out.printf("============= MAP %d =============\n", num + 1);
            for (int[] mapLine: maps.get(num)) {
                for (int cell: mapLine) {
                    System.out.print(cell);
                }
                System.out.println();
            }
        }
    }
}