package edu.java.response;

import edu.java.client.api.LinkUpdateEvent;
import java.net.URI;
import java.util.List;

public interface LinkUpdateResponse {
    List<LinkUpdateEvent> pullUpdates(URI url);
}
