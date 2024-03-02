package edu.java.client.stackoverflow;

import edu.java.client.Client;
import java.util.List;

public class StackoverflowClient extends Client {
    public StackoverflowClient(String url, List<StackoverflowClient.SubClient> subClients) {
        super(url, subClients);
    }

    public interface SubClient extends edu.java.client.SubClient {
    }
}
