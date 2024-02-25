package edu.java.bot;

import edu.java.bot.exceptions.DomainIsNotSupportedException;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.LinkProcessor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LinkHandlerTest extends AbstractTest {
    @Test
    public void linkHandlerChainShouldTraverseAllHandlers() {
        assertThatCode(() -> {
            linkProcessor.getDomain("https://github.com");
            linkProcessor.getDomain("https://stackoverflow.com");
        }).doesNotThrowAnyException();
    }

    @Test
    public void hanThrowsExceptionOnUnexpectedUrl() {
        assertThatThrownBy(() -> linkProcessor.getDomain("https://foo.bar"))
            .isInstanceOf(DomainIsNotSupportedException.class);
    }
}
