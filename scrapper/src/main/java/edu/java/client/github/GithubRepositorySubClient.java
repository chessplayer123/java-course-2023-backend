package edu.java.client.github;

import edu.java.link.LinkInfoSupplier;
import java.net.URL;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GithubRepositorySubClient implements GithubClient.SubClient {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    @Override
    public Pattern getUrlPattern() {
        return REPOSITORY_URL_PATTERN;
    }

    @Override
    public String convertUrlToApiPath(URL url) {
        return supports(url) ? "/repos/%s".formatted(url.getPath()) : null;
    }

    @Override
    public Class<? extends LinkInfoSupplier> getInfoSupplierType() {
        return GithubRepositoryInfo.class;
    }

}
