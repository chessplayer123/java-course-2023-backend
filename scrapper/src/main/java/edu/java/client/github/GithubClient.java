package edu.java.client.github;

import edu.java.client.Client;
import java.util.List;

public class GithubClient extends Client {
    public GithubClient(String url, List<GithubLinkHandler> handlers) {
        super(url, handlers);
    }
}
