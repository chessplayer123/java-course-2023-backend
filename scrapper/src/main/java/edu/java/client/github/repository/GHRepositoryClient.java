package edu.java.client.github.repository;

import edu.java.client.Client;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URL;
import java.util.regex.Pattern;

@Component
public class GHRepositoryClient extends Client<GHRepository> {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    public GHRepositoryClient(String baseUrl) {
        super(baseUrl);
    }

    public GHRepositoryClient() {
        super("https://api.github.com");
    }

    @Override
    @Nullable
    public GHRepository fetch(URL url) {
        if (!supports(url)) {
            return null;
        }
        return sendRequest("/repos/%s".formatted(url.getPath()), GHRepository.class);
    }

    @Override
    public boolean supports(URL url) {
        return REPOSITORY_URL_PATTERN.matcher(url.toString()).matches();
    }
}
