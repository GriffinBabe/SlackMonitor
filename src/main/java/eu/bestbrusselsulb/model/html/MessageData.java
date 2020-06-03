package eu.bestbrusselsulb.model.html;

/**
 * Message data collection to send through a websocket
 * as a Json string.
 */
public class MessageData {

    private String avatar;
    private String username;
    private String channel;
    private String content;

    public MessageData(String avatar, String username, String channel, String content) {
        this.avatar = avatar;
        this.username = username;
        this.channel = channel;
        this.content = content;
    }
}
