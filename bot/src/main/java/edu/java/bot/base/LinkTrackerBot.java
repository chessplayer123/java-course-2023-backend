package edu.java.bot.base;

import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.LinkHandler;
import edu.java.bot.link.StackOverflowLinkHandler;
import edu.java.bot.processor.FsmChatProcessor;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.user.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LinkTrackerBot extends Bot {
    public LinkTrackerBot(String token) {
        super(token, new FsmChatProcessor());
    }

    @PostConstruct
    @Override
    protected void setup() {
        UserService userService = new InMemoryUserService();

        LinkHandler linkHandler = LinkHandler.newChain()
            .addHandler(new GithubLinkHandler())
            .addHandler(new StackOverflowLinkHandler());

        chatProcessor
            .addCommand(new ListCommand(userService))
            .addCommand(new StartCommand(userService))
            .addCommand(new TrackCommand(userService, linkHandler))
            .addCommand(new UnTrackCommand(userService))
            .buildHelpCommand("This bot is designed to track added links.\nHere's the list of available commands:");

        start();
    }

}
