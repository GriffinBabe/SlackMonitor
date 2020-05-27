package eu.bestbrusselsulb;


import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.ChannelCreatedEvent;
import com.slack.api.model.event.MessageEvent;
import eu.bestbrusselsulb.controller.ChannelController;
import eu.bestbrusselsulb.controller.HelloCommandController;
import eu.bestbrusselsulb.controller.MessageController;

public class SlackMonitor {

    private App app = null;
    private SlackAppServer server = null;

    private HelloCommandController helloController;
    private MessageController messageController;
    private ChannelController channelController;

    public SlackMonitor() {
        this.initControllers();
        this.app = new App();

        this.app.command("/hello", this.helloController);

        this.app.event(MessageEvent.class, this.messageController);

        this.app.event(ChannelCreatedEvent.class, this.channelController);

        this.server = new SlackAppServer(app);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initControllers() {
        this.helloController = new HelloCommandController();
        this.messageController = new MessageController();
        this.channelController = new ChannelController();
    }

    public static void main(String[] args) {
        SlackMonitor monitor = new SlackMonitor();
    }
}
