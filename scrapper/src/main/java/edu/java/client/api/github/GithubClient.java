package edu.java.client.api.github;

import edu.java.client.api.ApiClient;
import edu.java.client.api.github.handlers.GithubLinkHandler;
import java.util.List;

public class GithubClient extends ApiClient {
    public GithubClient(String url, List<GithubLinkHandler> handlers) {
        super(url, handlers);
    }
}
