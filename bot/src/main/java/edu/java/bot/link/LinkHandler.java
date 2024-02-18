package edu.java.bot.link;

public abstract class LinkHandler {
    private LinkHandler nextHandler;

    public static LinkHandler newChain() {
        return new LinkHandler() {
            @Override
            protected boolean parseUrl(String url) {
                return false;
            }

            @Override
            protected String getDomain() {
                return null;
            }
        };
    }

    protected abstract boolean parseUrl(String url);

    protected abstract String getDomain();

    public String getDomain(String url) throws DomainIsNotSupportedException {
        if (parseUrl(url)) {
            return getDomain();
        } else if (nextHandler != null) {
            return nextHandler.getDomain(url);
        }
        throw new DomainIsNotSupportedException();
    }

    public LinkHandler addHandler(LinkHandler next) {
        // Adding in reverse to allow:
        // handler =  ... .addNextHandler() ... .addNextHandler();
        next.nextHandler = this;
        return next;
    }
}
