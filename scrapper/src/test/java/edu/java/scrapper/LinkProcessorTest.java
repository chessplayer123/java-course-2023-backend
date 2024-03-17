package edu.java.scrapper;

import edu.java.ScrapperApplication;
import edu.java.client.api.github.GithubClient;
import edu.java.client.api.stackoverflow.StackoverflowClient;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.link.LinkProcessor;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqSubscriptionRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import lombok.SneakyThrows;
import org.apache.commons.validator.Arg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ScrapperApplication.class})
public class LinkProcessorTest {
    @Autowired
    LinkProcessor processor;

    static Arguments[] clientsUrl() {
        return new Arguments[] {
            Arguments.of(
                URI.create("https://github.com/chessplayer123/java-course-2023-backend"), GithubClient.class
            ),
            Arguments.of(
                URI.create("https://stackoverflow.com/questions/44593066/spring-webflux-webclient-get-body-on-error"), StackoverflowClient.class
            ),
        };
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("clientsUrl")
    public void processorShouldReturnGithubClientWhenGithubRepositoryUrlGiven(URI url, Class<?> clientClass) {
        assertThat(processor.findClient(url))
            .isInstanceOf(clientClass);
    }

    @Test
    public void processorShouldThrowExceptionOnUnsupportedUrl() {
        URI link = URI.create("https://foo.bar");

        assertThatThrownBy(() -> processor.findClient(link))
            .isInstanceOf(LinkIsNotSupportedException.class);
    }
}
