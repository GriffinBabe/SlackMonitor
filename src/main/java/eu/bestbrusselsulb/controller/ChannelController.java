package eu.bestbrusselsulb.controller;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsJoinResponse;
import com.slack.api.model.event.ChannelCreatedEvent;

import java.io.IOException;

public class ChannelController extends EventController {
    @Override
    public Response apply(EventsApiPayload event, EventContext context) throws IOException, SlackApiException {
        ChannelCreatedEvent ev =(ChannelCreatedEvent) event.getEvent();
        ConversationsJoinResponse join = context.client().conversationsJoin(r -> r.channel(ev.getChannel().getId()));
        if (!join.isOk()) {
            System.out.format("Channel created: %s, couldn't join: %s.\n", ev.getChannel().getName(), join.getError());
        } else {
            System.out.format("Channel created: %s, joined.\n", ev.getChannel().getName());
        }
        return context.ack();
    }
}
