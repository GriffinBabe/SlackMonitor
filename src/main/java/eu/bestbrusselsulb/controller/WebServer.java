package eu.bestbrusselsulb.controller;

import com.google.gson.Gson;
import eu.bestbrusselsulb.model.SlackMonitor;
import eu.bestbrusselsulb.model.html.MessageData;
import eu.bestbrusselsulb.utils.EventEmitter;
import eu.bestbrusselsulb.utils.EventListener;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class WebServer implements EventListener {

    private static int PORT = 2700;

    private SlackMonitor slackMonitor = null;
    private Javalin app = null;
    private boolean slackMonitorEnabled = false;

    private static Map<WsContext, Integer> userMap = new ConcurrentHashMap<>();
    private static int usernameCount = 1;

    WebServer() {
        initializeMonitor();
        initializeWeb();
        initializeWebSocket();
    }

    /**
     * Starts slack monitor on a different thread.
     */
    private void initializeMonitor() {
        slackMonitor = new SlackMonitor();
        slackMonitor.getHandler(SlackMonitor.NEW_MESSAGE_HANDLER).addListener(this);

        Thread slack = new Thread(() -> {
           try {
               slackMonitorEnabled = true;
               slackMonitor.start();
           } catch (Exception e) {
               System.out.format("Couldn't start SlackMonitor :(");
               e.printStackTrace();
               slackMonitorEnabled = false;
           }
        });

        slack.start();
    }

    /**
     * Initializes GET and POST routes.
     */
    private void initializeWeb() {
        app = Javalin.create().start(PORT);
        app.config.addStaticFiles("/static", Location.CLASSPATH);

        app.get("/", (ctx) -> {
            ctx.render("/static/index.html");
        });
    }

    /**
     * Initializes websocket event listeners.
     */
    private void initializeWebSocket() {
        app.ws("/websocket/message-socket", ws -> {

            ws.onConnect(ctx -> {
                System.out.format("Client connected!\n");
                userMap.put(ctx, usernameCount++);
            });

            ws.onClose(ctx -> {
                System.out.println("Client closed!\n");
                userMap.remove(ctx); // same ctx for same user.
            });

        });
    }

    public static void main(String[] args) {
        new WebServer();
    }

    @Override
    public void onEvent(Object emitter, EventEmitter.EventType type, Object... args) {
        if (type == EventEmitter.EventType.MESSAGE_RECEIVED) {
            MessageData message = (MessageData) args[0];
            broadCastMessage(message);
        }
    }

    /**
     * Sends {@link MessageData} data to all the websocket sessions
     * through JSON.
     *
     * @param data, the {@link MessageData} object to send.
     */
    private void broadCastMessage(MessageData data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        userMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
            session.send(json); // sends data mesasge
        });
    }
}
