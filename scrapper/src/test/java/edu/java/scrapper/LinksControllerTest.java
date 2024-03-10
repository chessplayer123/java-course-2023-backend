package edu.java.scrapper;

import edu.java.client.api.ApiClient;
import edu.java.controller.LinksController;
import edu.java.link.LinkProcessor;
import edu.java.response.LinkInfo;
import edu.java.service.Link;
import edu.java.service.LinkService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.net.URI;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinksController.class)
public class LinksControllerTest  {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LinkProcessor processor;

    @MockBean
    LinkService service;

    @Test
    @SneakyThrows
    public void trackLinkShouldCallLinkServiceMethod() {
        URI url = URI.create("https://github.com");
        Long chatId = 123L;
        ApiClient client = Mockito.mock(ApiClient.class);
        LinkInfo info = Mockito.mock(LinkInfo.class);
        Link expectedLink = new Link(0L, info);

        Mockito.when(processor.findClient(url)).thenReturn(client);
        Mockito.when(client.fetch(url)).thenReturn(info);
        Mockito.when(service.track(chatId, info)).thenReturn(expectedLink);
        Mockito.when(info.getLink()).thenReturn(url);

        mockMvc.perform(post("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .content("""
                {
                    "link": "%s"
                }
                """.formatted(url.toString()))
                .contentType("application/json")
            )
            .andExpect(status().isOk())
            .andExpect(content().json("""
            {
                "id": %d,
                "url": "%s"
            }""".formatted(expectedLink.id(), expectedLink.info().getLink())));

        Mockito
            .verify(service)
            .track(chatId, info);
    }

    @Test
    @SneakyThrows
    public void untrackLinkShouldCallLinkServiceMethod() {
        URI url = URI.create("https://stackoverflow.com");
        Long chatId = 123L;
        LinkInfo info = Mockito.mock(LinkInfo.class);
        Link removedLink = new Link(0L, info);

        Mockito.when(info.getLink()).thenReturn(url);
        Mockito.when(service.untrack(chatId, url)).thenReturn(removedLink);

        mockMvc.perform(delete("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .content("""
                {
                    "link": "%s"
                }
                """.formatted(url.toString()))
                .contentType("application/json")
            )
            .andExpect(status().isOk())
            .andExpect(content().json("""
            {
                "id": %d,
                "url": "%s"
            }""".formatted(removedLink.id(), removedLink.info().getLink())));

        Mockito
            .verify(service)
            .untrack(chatId, url);
    }

    @Test
    @SneakyThrows
    public void listLinksShouldReturnExpectedResponse() {
        Long chatId = 123L;

        LinkInfo link1 = Mockito.mock(LinkInfo.class);
        Mockito.when(link1.getLink()).thenReturn(URI.create("https://github.com"));

        LinkInfo link2 = Mockito.mock(LinkInfo.class);
        Mockito.when(link2.getLink()).thenReturn(URI.create("https://stackoverflow.com"));

        Mockito.when(service.listAll(chatId)).thenReturn(List.of(
            new Link(0L, link1),
            new Link(1L, link2)
        ));

        mockMvc
            .perform(get("/links").header("Tg-Chat-Id", String.valueOf(chatId)))
            .andExpect(status().isOk())
            .andExpect(content().json("""
            {
                "links": [
                    {
                        "id": 0,
                        "url": "https://github.com"
                    },
                    {
                        "id": 1,
                        "url": "https://stackoverflow.com"
                    }
                ],
                "size": 2
            }"""));

        Mockito
            .verify(service)
            .listAll(chatId);
    }
}
