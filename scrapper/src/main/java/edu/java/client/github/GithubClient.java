package edu.java.client.github;

import edu.java.client.Client;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.net.URL;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GithubClient extends Client {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    public GithubClient(String baseUrl) {
        super(baseUrl);
    }

    public GithubClient() {
        super("https://api.github.com");
    }

    @Override
    @Nullable
    public LinkInfoSupplier fetch(URL url) {
        if (REPOSITORY_URL_PATTERN.matcher(url.toString()).matches()) {
            return sendRequest("/repos/%s".formatted(url.getPath()), GHRepository.class);
        }
        return null;
    }

    @Override
    public boolean supports(URL url) {
        return REPOSITORY_URL_PATTERN.matcher(url.toString()).matches();
    }
}
