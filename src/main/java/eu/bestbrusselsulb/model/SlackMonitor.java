package eu.bestbrusselsulb.model;


import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.ChannelCreatedEvent;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.model.event.ReactionAddedEvent;
import eu.bestbrusselsulb.model.handlers.*;

import java.util.HashMap;
import java.util.Map;

public class SlackMonitor {

    private App app = null;
    private SlackAppServer server = null;

    private Map<String, EventHandler> handlers = new HashMap<>();

    public static String NEW_MESSAGE_HANDLER = "newMessageHandler";
    public static String NEW_CONVERSATION_HANDLER = "newConversationHandler";
    public static String REACTION_HANDLER = "reactionHandler";

    public SlackMonitor() {
        this.initHandlers();
    }

    public void start() throws Exception {
        this.app = new App();

        this.app.event(MessageEvent.class, this.handlers.get(NEW_MESSAGE_HANDLER));

        this.app.event(ChannelCreatedEvent.class, this.handlers.get(NEW_CONVERSATION_HANDLER));

        this.app.event(ReactionAddedEvent.class, this.handlers.get(REACTION_HANDLER));

        this.server = new SlackAppServer(app);

        server.start();
    }

    private void initHandlers() {
        this.handlers.put(NEW_MESSAGE_HANDLER, new NewMessageHandler());
        this.handlers.put(NEW_CONVERSATION_HANDLER, new NewConversationHandler());
        this.handlers.put(REACTION_HANDLER, new ReactionHandler());
    }

    public EventHandler getHandler(String handlerName) {
        return handlers.get(handlerName);
    }

    public static void main(String[] args) {
        SlackMonitor monitor = new SlackMonitor();
    }
}
