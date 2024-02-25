package edu.java.bot.link;

import edu.java.bot.exceptions.DomainIsNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkHandlerChain {
    private final List<LinkHandler> handlers;

    public LinkHandlerChain() {
        handlers = new ArrayList<>();
    }

    public LinkHandlerChain(LinkHandler... handlers) {
        this.handlers = List.of(handlers);
    }

    @Autowired
    public LinkHandlerChain(
        GithubLinkHandler githubLinkHandler,
        StackOverflowLinkHandler stackOverflowLinkHandler
    ) {
        handlers = List.of(githubLinkHandler, stackOverflowLinkHandler);
    }

    public String getDomain(String url) throws DomainIsNotSupportedException {
        for (LinkHandler handler : handlers) {
            if (handler.supports(url)) {
                return handler.getDomain();
            }
        }
        throw new DomainIsNotSupportedException();
    }
}
