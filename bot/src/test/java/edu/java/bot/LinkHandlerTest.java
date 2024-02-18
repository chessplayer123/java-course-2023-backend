package edu.java.bot;

import edu.java.bot.link.DomainIsNotSupportedException;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.LinkHandler;
import edu.java.bot.link.StackOverflowLinkHandler;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LinkHandlerTest {
    @Test
    public void linkHandlerChainShouldTraverseAllHandlers() {
        LinkHandler linkHandler = LinkHandler.newChain()
            .addHandler(new GithubLinkHandler())
            .addHandler(new StackOverflowLinkHandler());

        assertThatCode(() -> {
            linkHandler.getDomain("https://github.com");
            linkHandler.getDomain("https://stackoverflow.com");
        }).doesNotThrowAnyException();
    }

    @Test
    public void githubLinkHandlerThrowsExceptionOnUnexpectedUrl() {
        LinkHandler linkHandler = new GithubLinkHandler();

        assertThatThrownBy(() -> linkHandler.getDomain("https://stackoverflow.com"))
            .isInstanceOf(DomainIsNotSupportedException.class);
    }

    @Test
    public void stackOverFlowLinkHandlerThrowsExceptionOnUnexpectedUrl() {
        LinkHandler linkHandler = new StackOverflowLinkHandler();

        assertThatThrownBy(() -> linkHandler.getDomain("https://github.com"))
            .isInstanceOf(DomainIsNotSupportedException.class);
    }
}
