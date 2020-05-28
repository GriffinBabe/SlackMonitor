package eu.bestbrusselsulb.model;


import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.ChannelCreatedEvent;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.model.event.ReactionAddedEvent;
import eu.bestbrusselsulb.model.handlers.NewConversationHandler;
import eu.bestbrusselsulb.model.handlers.NewMessageHandler;
import eu.bestbrusselsulb.model.handlers.ReactionHandler;

public class SlackMonitor {

    private App app = null;
    private SlackAppServer server = null;

    private NewMessageHandler newMessageController;
    private NewConversationHandler channelController;
    private ReactionHandler reactionController;

    public SlackMonitor() {
        this.initControllers();
        this.app = new App();

        this.app.event(MessageEvent.class, this.newMessageController);

        this.app.event(ChannelCreatedEvent.class, this.channelController);

        this.app.event(ReactionAddedEvent.class, this.reactionController);

        this.server = new SlackAppServer(app);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initControllers() {
        this.newMessageController = new NewMessageHandler();
        this.channelController = new NewConversationHandler();
        this.reactionController = new ReactionHandler();
    }

    public static void main(String[] args) {
        SlackMonitor monitor = new SlackMonitor();
    }
}
