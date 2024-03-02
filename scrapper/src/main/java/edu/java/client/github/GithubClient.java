package edu.java.client.github;

import edu.java.client.Client;
import edu.java.link.LinkInfoSupplier;
import java.util.List;

public class GithubClient extends Client {
    public GithubClient(String url, List<GithubClient.SubClient> subClients) {
        super(url, subClients);
    }

    public interface SubClient extends edu.java.client.SubClient {
        @Override
        default Class<? extends LinkInfoSupplier> getInfoSupplierType() {
            return GithubRepositoryInfo.class;
        }

    }
}
