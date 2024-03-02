package edu.java.client.github;

import edu.java.client.Client;
import java.util.List;

public class GithubClient extends Client {
    public GithubClient(String url, List<GithubClient.SubClient> subClients) {
        super(url, subClients);
    }

    public interface SubClient extends edu.java.client.SubClient {
    }
}
