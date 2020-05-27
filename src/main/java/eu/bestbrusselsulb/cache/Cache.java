package eu.bestbrusselsulb.cache;

import com.slack.api.model.Conversation;
import com.slack.api.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains {@link com.slack.api.model.User} and {@link com.slack.api.model.Conversation}
 * information to avoid fetching the data each time.
 */
public class Cache {

    private static Cache instance = null;

    Map<String, User> userMap = null;
    Map<String, Conversation> conversationMap = null;

    private Cache() {
      userMap = new HashMap<>();
      conversationMap = new HashMap<>();
    }

    public static Cache getInstace() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public User getUser(String id) {
        return userMap.get(id);
    }

    public Conversation getConversation(String id) {
        return conversationMap.get(id);
    }

    public void addUser(String name, User user) {
        userMap.put(name, user);
    }

    public void addConversation(String name, Conversation conversation) {
        conversationMap.put(name, conversation);
    }

}
