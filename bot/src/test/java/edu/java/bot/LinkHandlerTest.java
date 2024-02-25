package edu.java.bot;

import edu.java.bot.exceptions.DomainIsNotSupportedException;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.LinkHandlerChain;
import edu.java.bot.link.StackOverflowLinkHandler;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LinkHandlerTest {
    @Test
    public void linkHandlerChainShouldTraverseAllHandlers() {
        LinkHandlerChain linkHandler = new LinkHandlerChain(
            new GithubLinkHandler(),
            new StackOverflowLinkHandler()
        );

        assertThatCode(() -> {
            linkHandler.getDomain("https://github.com");
            linkHandler.getDomain("https://stackoverflow.com");
        }).doesNotThrowAnyException();
    }

    @Test
    public void githubLinkHandlerThrowsExceptionOnUnexpectedUrl() {
        LinkHandlerChain linkHandlerChain = new LinkHandlerChain(new GithubLinkHandler());

        assertThatThrownBy(() -> linkHandlerChain.getDomain("https://stackoverflow.com"))
            .isInstanceOf(DomainIsNotSupportedException.class);
    }

    @Test
    public void stackOverFlowLinkHandlerThrowsExceptionOnUnexpectedUrl() {
        LinkHandlerChain linkHandlerChain = new LinkHandlerChain(new StackOverflowLinkHandler());

        assertThatThrownBy(() -> linkHandlerChain.getDomain("https://github.com"))
            .isInstanceOf(DomainIsNotSupportedException.class);
    }
}
