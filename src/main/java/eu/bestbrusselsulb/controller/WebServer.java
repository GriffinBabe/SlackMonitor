package eu.bestbrusselsulb.controller;

import eu.bestbrusselsulb.model.SlackMonitor;
import eu.bestbrusselsulb.utils.EventEmitter;
import eu.bestbrusselsulb.utils.EventListener;
import io.javalin.Javalin;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

class WebServer implements EventListener {

    private static int PORT = 2700;

    private SlackMonitor slackMonitor = null;
    private Javalin app = null;
    private boolean slackMonitorEnabled = false;

    WebServer() {
        // initializeMonitor();
        initializeWeb();
    }

    /**
     * Starts slack monitor on a different thread.
     */
    private void initializeMonitor() {
        slackMonitor = new SlackMonitor();
        Thread slack = new Thread(() -> {
           try {
               slackMonitor.start();
               slackMonitorEnabled = true;
               slackMonitor.getHandler(SlackMonitor.NEW_MESSAGE_HANDLER).addListener(this);
           } catch (Exception e) {
               System.out.format("Couldn't start SlackMonitor :(");
               e.printStackTrace();
           }
        });
        slack.start();
    }

    private void initializeWeb() {
        app = Javalin.create().start(PORT);
        app.config.addStaticFiles(WebServer.class.getResource("/index.html").toString().replace("file:/" ,"").replace("/index.html", "/"));
        app.get("/", (ctx) -> {
            ctx.redirect("/index.html");
        });
    }

    public static void main(String[] args) {
        new WebServer();
    }

    @Override
    public void onEvent(Object emitter, EventEmitter.EventType type, Object... args) {
        // TODO implement new messages
    }
}
