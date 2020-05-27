package eu.bestbrusselsulb.controller;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import com.slack.api.model.event.MessageEvent;
import eu.bestbrusselsulb.cache.Cache;

import java.io.IOException;

public class MessageController extends EventController {

    @Override
    public Response apply(EventsApiPayload event, EventContext context) throws IOException, SlackApiException {
        MessageEvent ev = (MessageEvent) event.getEvent();
        String messageText = ev.getText();
        String channelId = ev.getChannel();
        String userId = ev.getUser();

        Cache cache = Cache.getInstace();
        User user = cache.getUser(userId);
        Conversation conversation = cache.getConversation(channelId);
        if (user == null) {
            UsersInfoResponse response = context.client().usersInfo(r -> r.user(userId));
            if (!response.isOk()) {
                System.err.format("Couldn't fetch data on user: %s. Cause: %s", userId, response.getError());
                return context.ack();
            }
            cache.addUser(userId, response.getUser());
            user = response.getUser();
        }
        if (conversation == null) {
            ConversationsInfoResponse response = context.client().conversationsInfo(r -> r.channel(channelId));
            if (!response.isOk()) {
                System.err.format("Couldn't fetch data on channel: %s. Cause: %s", channelId, response.getError());
                return context.ack();
            }
            cache.addConversation(channelId, response.getChannel());
            conversation = response.getChannel();
        }


        System.out.format("<%s> %s: %s\n", conversation.getName(), user.getName(), messageText);
        return context.ack();
    }

}
