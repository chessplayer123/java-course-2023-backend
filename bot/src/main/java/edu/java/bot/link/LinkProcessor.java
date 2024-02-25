package edu.java.bot.link;

import edu.java.bot.exceptions.DomainIsNotSupportedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkProcessor {
    private final List<LinkHandler> handlers;

    public String getDomain(String url) throws DomainIsNotSupportedException {
        for (LinkHandler handler : handlers) {
            if (handler.supports(url)) {
                return handler.getDomain();
            }
        }
        throw new DomainIsNotSupportedException();
    }
}
