package edu.java.handlers;

import edu.java.response.LinkInfo;
import java.net.URL;
import java.util.regex.Pattern;

public abstract class LinkHandler {
    protected abstract Pattern getUrlPattern();

    public abstract Class<? extends LinkInfo> getResponseType();

    public abstract String convertUrlToApiPath(URL url);

    public boolean supports(URL url) {
        return getUrlPattern().matcher(url.toString()).matches();
    }
}
