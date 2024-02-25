package edu.java.bot.link;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowLinkHandler implements LinkHandler {
    private static final Pattern STACKOVERFLOW_URL_PATTERN = Pattern.compile(".*stackoverflow\\.com.*");

    @Override
    public boolean supports(String url) {
        return STACKOVERFLOW_URL_PATTERN.matcher(url).matches();
    }

    @Override
    public String getDomain() {
        return "stackoverflow.com";
    }
}
