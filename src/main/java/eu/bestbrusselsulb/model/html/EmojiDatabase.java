package eu.bestbrusselsulb.model.html;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bestbrusselsulb.controller.MonitorApplication;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class containing all the Emoji data.
 */
public class EmojiDatabase {

    private Map<String, String> database = new HashMap<>();

    private static EmojiDatabase instance;

    private EmojiDatabase() {
        try {
            initializeData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Collects the emoji data in the emoji.json file.
     * The file contains all necessary data to render our
     * emojis in html.
     *
     * @throws IOException if there is a problem when parsing the data
     */
    private void initializeData() throws IOException {
        // parses the data in the emoji.json class
        Gson gson = new Gson();
        String url = MonitorApplication.class.getResource("/emoji.json").toString().replace("file:/", "");
        Reader reader = new FileReader(url);

        Type listType = new TypeToken<ArrayList<EmojiData>>(){}.getType();
        List<EmojiData> emojis = gson.fromJson(reader, listType);
        collectEmojiData(emojis);
    }

    /**
     * Sub function of {@link #initializeData()}.
     * Parses the data retrieved from the json file.
     * @param data data to be parsed.
     */
    private void collectEmojiData(List<EmojiData> data) {
        int collectedCount = 0;
        for (EmojiData emoji : data) {
            // converts the unified hexadecimal number to decimal
            String unified = emoji.getUnified();
            Integer unifiedInt;
            try {
                unifiedInt = Integer.parseInt(unified, 16);
                if (unifiedInt == null) continue; // skips if the
            } catch (NumberFormatException e) {
                continue;
            }

            for (String name : emoji.getShortNames()) { // emoji can have multiple colon names.
                database.put(name, unifiedInt.toString());
            }
            collectedCount++;
        }
        System.out.format("Collected %d emojis from emoji.json.\n", collectedCount);
    }

    /**
     * Gathers the corresponding hexadecimal value
     * of the given colon named emoji. This is used
     * to convert slack emojis to hexadecimal emojis.
     *
     * Returns null if there is no match.
     *
     * @param colonName the colon name of the emoji
     * @return the hexadecimal code of the emoji.
     */
    public String getDecimalEmoji(String colonName) {
        return database.get(colonName);
    }

    /**
     * Singleton get instance function.
     * Initiates the database if this hasn't been done yet.
     *
     * @return an instance of {@link EmojiDatabase}.
     */
    public static EmojiDatabase getInstance() {
        if (instance == null) {
            instance = new EmojiDatabase();
        }
        return instance;
    }
}
