package edu.java.client.github;

import edu.java.client.SubClient;
import edu.java.link.LinkInfoSupplier;
import java.net.URL;
import java.util.regex.Pattern;

public class GithubRepositorySubClient implements SubClient {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    @Override
    public Pattern getUrlPattern() {
        return REPOSITORY_URL_PATTERN;
    }

    @Override
    public Class<? extends LinkInfoSupplier> getInfoSupplierType() {
        return GithubRepository.class;
    }

    @Override
    public String getApiPath(URL url) {
        if (supports(url)) {
            return "/repos/%s".formatted(url.getPath());
        }
        return null;
    }
}
