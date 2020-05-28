package eu.bestbrusselsulb.model.service;

import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;

import java.io.IOException;

/**
 * Fetches the data either by accessing the {@link Cache}, or by
 * asking the Slack api and then storing to the {@link Cache}.
 */
public class DataFetcher {

    private Cache cache = null;

    private static DataFetcher instance = null;

    private DataFetcher() {
        this.cache = Cache.getInstace();
    }

    public static DataFetcher getInstance() {
        if (instance == null) {
            instance = new DataFetcher();
        }
        return instance;
    }

    public User getUser(String id, EventContext context) throws IOException, SlackApiException {
        User user = cache.getUser(id);
        if (user == null) {
            UsersInfoResponse response = context.client().usersInfo(r -> r.user(id));
            if (!response.isOk()) {
                System.err.format("Couldn't fetch data on user: %s. Cause: %s", id, response.getError());
                return null;
            }
            cache.addUser(id, response.getUser());
            user = response.getUser();
        }
        return user;
    }

    public Conversation getConversation(String id, EventContext context) throws IOException, SlackApiException {
        Conversation conversation = cache.getConversation(id);
        if (conversation == null) {
            ConversationsInfoResponse response = context.client().conversationsInfo(r -> r.channel(id));
            if (!response.isOk()) {
                System.err.format("Couldn't fetch data on channel: %s. Cause: %s", id, response.getError());
                return null;
            }
            cache.addConversation(id, response.getChannel());
            conversation = response.getChannel();
        }
        return conversation;
    }

    public Message getMessage(String id, EventContext context) throws IOException, SlackApiException {
        Message message = cache.getMessage(id);
        return message;
    }
}
