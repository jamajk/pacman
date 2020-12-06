package prozeman;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.ArrayList;

public class Config {
    static int playerspeed_easy;
    static int playerspeed_medium;
    static int playerspeed_hardcore;
    static int ghostspeed_easy;
    static int ghostspeed_medium;
    static int ghostspeed_hardcore;
    static int numberofhearts_easy;
    static int numberofhearts_medium;
    static int numberofhearts_hardcore;
    static int numberofghosts_easy;
    static int numberofghosts_medium;
    static int numberofghosts_hardcore;

    static int numberoflevels;
    static ArrayList<int[][]> maps;


    static void loadConfig() throws IOException {
        InputStream file = new FileInputStream("src/configs/config1.txt");
        Properties config = new Properties();
        config.load(file);
        playerspeed_easy=Integer.parseInt(config.getProperty("playerspeed-e"));
        playerspeed_medium=Integer.parseInt(config.getProperty("playerspeed-m"));
        playerspeed_hardcore=Integer.parseInt(config.getProperty("playerspeed-h"));
        ghostspeed_easy=Integer.parseInt(config.getProperty("ghostspeed-e"));
        ghostspeed_medium=Integer.parseInt(config.getProperty("ghostspeed-m"));
        ghostspeed_hardcore=Integer.parseInt(config.getProperty("ghostspeed-h"));
        numberofhearts_easy=Integer.parseInt(config.getProperty("nohearts-e"));
        numberofhearts_medium=Integer.parseInt(config.getProperty("nohearts-m"));
        numberofhearts_hardcore=Integer.parseInt(config.getProperty("nohearts-h"));
        numberofghosts_easy=Integer.parseInt(config.getProperty("noghosts-e"));
        numberofghosts_medium=Integer.parseInt(config.getProperty("noghosts-m"));
        numberofghosts_hardcore=Integer.parseInt(config.getProperty("noghosts-h"));

        numberoflevels=Integer.parseInt(config.getProperty("nolevels"));

        file.close();

        maps = new ArrayList<int[][]>();
        for (int n = 0; n < numberoflevels; n++) {
            loadMap(n);
        }
    }

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
            System.out.println("No file found: " + "src/configs/map.txt");
        }
    }

    static void printLoadedConfig() {
        System.out.println(playerspeed_easy);
        System.out.println(playerspeed_medium);
        System.out.println(playerspeed_hardcore);

        System.out.println(ghostspeed_easy);
        System.out.println(ghostspeed_medium);
        System.out.println(ghostspeed_hardcore);

        System.out.println(numberofhearts_easy);
        System.out.println(numberofhearts_medium);
        System.out.println(numberofhearts_hardcore);

    }

    static void printMaps() {
        for (int num = 0; num < maps.size(); num++) {
            System.out.printf("============= MAP %d =============\n", num + 1);
            for (int[] mapLine: maps.get(num)) {
                for (int cell: mapLine) {
                    System.out.print(cell);
                }
                System.out.println("");
            }
        }
    }
}