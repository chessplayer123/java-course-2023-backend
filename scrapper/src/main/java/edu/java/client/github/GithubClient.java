package edu.java.client.github;

import edu.java.client.Client;
import java.util.List;

public class GithubClient extends Client {
    public GithubClient(String url, List<GithubSubClient> subClients) {
        super(url, subClients);
    }
}
