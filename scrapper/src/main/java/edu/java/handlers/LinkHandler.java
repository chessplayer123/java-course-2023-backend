package edu.java.handlers;

import edu.java.client.api.ApiEndpoint;
import edu.java.exceptions.InvalidLinkException;
import edu.java.response.LinkApiResponse;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Pattern;

public abstract class LinkHandler {
    public abstract ApiEndpoint<? extends LinkApiResponse> retrieveInfoEndpoint(URI url) throws InvalidLinkException;

    public abstract List<ApiEndpoint<? extends LinkUpdateResponse>> retrieveUpdatesEndpoints(
        URI url,
        OffsetDateTime fromDate
    ) throws InvalidLinkException;

    protected abstract Pattern getUrlPattern();

    public boolean supports(URI url) {
        return getUrlPattern().matcher(url.toString()).matches();
    }
}
