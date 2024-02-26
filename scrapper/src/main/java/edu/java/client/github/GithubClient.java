package edu.java.client.github;

import edu.java.client.Client;
import edu.java.client.SubClient;
import java.util.List;

public class GithubClient extends Client {
    public GithubClient() {
        super("https://api.github.com", List.of(new GithubRepositorySubClient()));
    }

    public GithubClient(String url, List<SubClient> subClients) {
        super(url, subClients);
    }
}
