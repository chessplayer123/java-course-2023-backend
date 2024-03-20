package edu.java.client.api.stackoverflow;

import edu.java.client.api.ApiClient;
import edu.java.client.api.stackoverflow.handlers.StackoverflowLinkHandler;
import java.util.List;

public class StackoverflowClient extends ApiClient {
    public StackoverflowClient(String url, List<StackoverflowLinkHandler> handlers) {
        super(url, handlers);
    }
}
