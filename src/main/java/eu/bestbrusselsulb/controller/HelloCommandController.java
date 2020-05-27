package eu.bestbrusselsulb.controller;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;

public class HelloCommandController extends CommandController {
    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {
        return context.ack("SlackMonitor says hello from :flag-be:!");
    }
}
