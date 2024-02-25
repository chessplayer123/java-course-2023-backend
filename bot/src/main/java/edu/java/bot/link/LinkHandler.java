package edu.java.bot.link;

public interface LinkHandler {
    boolean supports(String url);

    String getDomain();
}
