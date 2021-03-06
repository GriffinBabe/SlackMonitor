package eu.bestbrusselsulb.model.handlers;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import com.slack.api.model.event.MessageEvent;
import eu.bestbrusselsulb.model.html.MessageData;
import eu.bestbrusselsulb.model.html.MessageRenderer;
import eu.bestbrusselsulb.model.service.DataFetcher;

import java.io.IOException;

public class NewMessageHandler extends EventHandler {

    @Override
    public Response apply(EventsApiPayload event, EventContext context) throws IOException, SlackApiException {
        MessageEvent ev = (MessageEvent) event.getEvent();
        String messageText = ev.getText();
        String messageId = ev.getClientMsgId();
        String channelId = ev.getChannel();
        String userId = ev.getUser();

        DataFetcher fetcher = DataFetcher.getInstance();
        User user = fetcher.getUser(userId, context);
        Conversation conversation = fetcher.getConversation(channelId, context);

        if (user == null || conversation == null) {
            return context.ack();
        }

        System.out.format("<%s> %s: %s\n", conversation.getName(), user.getName(), messageText);

        // Generates the message html code from the slack formatted message
        MessageRenderer renderer = new MessageRenderer(messageText);
        renderer.generate();

        MessageData messageData = new MessageData(
                user.getProfile().getImage72(),
                user.getName(),
                conversation.getName(),
                renderer.getHtmlCode(),
                messageId);

        emit(EventType.MESSAGE_RECEIVED, messageData);

        return context.ack();

    }

}
