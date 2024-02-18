package edu.java.bot.link;

import java.util.regex.Pattern;

public class StackOverflowLinkHandler extends LinkHandler {
    private static final Pattern STACKOVERFLOW_URL_PATTERN = Pattern.compile(".*stackoverflow\\.com.*");

    @Override
    protected boolean parseUrl(String url) {
        return STACKOVERFLOW_URL_PATTERN.matcher(url).matches();
    }

    @Override
    protected String getDomain() {
        return "stackoverflow.com";
    }
}
