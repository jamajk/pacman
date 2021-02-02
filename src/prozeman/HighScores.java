package prozeman;

import java.util.HashMap;
import java.util.Map;

public class HighScores {
    private HashMap<String, Integer> HS_Easy = new HashMap<String, Integer>();
    private HashMap<String, Integer> HS_Medium = new HashMap<String, Integer>();
    private HashMap<String, Integer> HS_Hard = new HashMap<String, Integer>();

    public HighScores() {

    }

    public void addHighscore(int diff, String name, int score) {
        switch (diff) {
            case 1:
                HS_Easy.put(name, score);
                break;
            case 2:
                HS_Medium.put(name, score);
                break;
            case 3:
                HS_Hard.put(name, score);
                break;
        }
    }
}
