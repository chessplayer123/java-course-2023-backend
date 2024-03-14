package edu.java.client.api.stackoverflow;

import edu.java.client.api.ApiClient;
import java.util.List;

public class StackoverflowClient extends ApiClient {
    public StackoverflowClient(String url, List<StackoverflowLinkHandler> subClients) {
        super(url, subClients);
    }
}
