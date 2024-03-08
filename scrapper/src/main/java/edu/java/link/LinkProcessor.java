package edu.java.link;

import edu.java.client.api.ApiClient;
import edu.java.exceptions.LinkIsNotSupportedException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkProcessor {
    private final List<ApiClient> apiClients;

    public ApiClient findClient(URL url) throws LinkIsNotSupportedException {
        return apiClients.stream()
            .filter(client -> client.supports(url))
            .findAny()
            .orElseThrow(LinkIsNotSupportedException::new);
    }
}
