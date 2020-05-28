package eu.bestbrusselsulb.model.service;

import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains {@link com.slack.api.model.User} and {@link com.slack.api.model.Conversation}
 * information to avoid fetching the data each time.
 */
class Cache {

    private static Cache instance = null;

    Map<String, User> userMap = null;
    Map<String, Conversation> conversationMap = null;
    Map<String, Message> messageMap = null;

    private Cache() {
      userMap = new HashMap<>();
      conversationMap = new HashMap<>();
      messageMap = new HashMap<>();
    }

    static Cache getInstace() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    User getUser(String id) {
        return userMap.get(id);
    }

    Conversation getConversation(String id) {
        return conversationMap.get(id);
    }

    Message getMessage(String id) { return messageMap.get(id); }

    void addUser(String id, User user) {
        userMap.put(id, user);
    }

    void addConversation(String id, Conversation conversation) {
        conversationMap.put(id, conversation);
    }

    void addMessage(String id, Message message) { messageMap.put(id, message); }

}
