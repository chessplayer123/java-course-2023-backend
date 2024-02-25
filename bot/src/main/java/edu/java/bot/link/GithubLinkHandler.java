package edu.java.bot.link;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GithubLinkHandler implements LinkHandler {
    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(".*github\\.com.*");

    @Override
    public boolean supports(String url) {
        return GITHUB_URL_PATTERN.matcher(url).matches();
    }

    @Override
    public String getDomain() {
        return "github.com";
    }
}
