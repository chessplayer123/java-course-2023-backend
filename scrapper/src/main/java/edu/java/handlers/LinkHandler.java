package edu.java.handlers;

import edu.java.response.LinkInfo;
import java.net.URI;
import java.util.regex.Pattern;

public abstract class LinkHandler {
    protected abstract Pattern getUrlPattern();

    public abstract Class<? extends LinkInfo> getResponseType();

    public abstract String convertUrlToApiPath(URI url);

    public boolean supports(URI url) {
        return getUrlPattern().matcher(url.toString()).matches();
    }
}
