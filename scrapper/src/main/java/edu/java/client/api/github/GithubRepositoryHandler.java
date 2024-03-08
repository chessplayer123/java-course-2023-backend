package edu.java.client.api.github;

import edu.java.response.LinkInfo;
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
    public Class<? extends LinkInfo> getResponseType() {
        return GithubRepositoryResponse.class;
    }

    @Override
    public String convertUrlToApiPath(URL url) {
        return "/repos/%s".formatted(url.getPath());
    }
}
