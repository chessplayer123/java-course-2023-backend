package edu.java.client.github;

import edu.java.response.Response;
import java.net.URL;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GithubRepositoryHandler extends GithubLinkHandler {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    @Override
    protected Pattern getUrlPattern() {
        return REPOSITORY_URL_PATTERN;
    }

    @Override
    public Class<? extends Response> getResponseType() {
        return GithubRepositoryResponse.class;
    }

    @Override
    public String convertUrlToApiPath(URL url) {
        return "/repos/%s".formatted(url.getPath());
    }
}
