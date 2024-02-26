package edu.java.client.stackoverflow;

import edu.java.client.Client;
import edu.java.client.SubClient;
import java.util.List;

public class StackOverflowClient extends Client {
    public StackOverflowClient() {
        super("https://api.stackexchange.com/2.3", List.of(new StackOverflowQuestionSubClient()));
    }

    public StackOverflowClient(String url, List<SubClient> subClients) {
        super(url, subClients);
    }
}
