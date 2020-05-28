package eu.bestbrusselsulb.model.handlers;


import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import com.slack.api.model.event.ReactionAddedEvent;
import eu.bestbrusselsulb.model.service.DataFetcher;

import java.io.IOException;

public class ReactionHandler extends EventHandler {
    @Override
    public Response apply(EventsApiPayload event, EventContext context) throws IOException, SlackApiException {
        ReactionAddedEvent ev = (ReactionAddedEvent) event.getEvent();

        DataFetcher fetcher = DataFetcher.getInstance();

        User userReacting = fetcher.getUser(ev.getUser(), context);
        User userTarget = fetcher.getUser(ev.getItemUser(), context);
        Conversation conversation = fetcher.getConversation(ev.getItem().getChannel(), context);
        String reaction = ev.getReaction();

        System.out.format("<%s> %s: Reacted %s to %s's post.\n",
                conversation.getName(), userReacting.getName(), reaction, userTarget.getName());
        return context.ack();
    }
}
