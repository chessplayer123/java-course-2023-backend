package edu.java.bot.link;

import java.util.regex.Pattern;

public class GithubLinkHandler extends LinkHandler {
    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(".*github\\.com.*");

    @Override
    protected boolean parseUrl(String url) {
        return GITHUB_URL_PATTERN.matcher(url).matches();
    }

    @Override
    protected String getDomain() {
        return "github.com";
    }
}
