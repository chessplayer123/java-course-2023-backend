package edu.java.client;

import edu.java.link.LinkInfoSupplier;
import java.net.URL;
import java.util.regex.Pattern;

public interface SubClient {
    Pattern getUrlPattern();

    String getApiPath(URL url);

    Class<? extends LinkInfoSupplier> getInfoSupplierType();

    default boolean supports(URL url) {
        return getUrlPattern().matcher(url.toString()).matches();
    }
}
