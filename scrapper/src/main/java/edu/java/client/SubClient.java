package edu.java.client;

import edu.java.link.LinkInfoSupplier;
import java.net.URL;
import java.util.regex.Pattern;

public abstract class SubClient {
    protected abstract Pattern getUrlPattern();

    public abstract String convertUrlToApiPath(URL url);

    public abstract Class<? extends LinkInfoSupplier> getInfoSupplierType();

    public boolean supports(URL url) {
        return getUrlPattern().matcher(url.toString()).matches();
    }
}
