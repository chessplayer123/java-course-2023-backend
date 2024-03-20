package edu.java.client.api.stackoverflow.handlers;

import edu.java.client.api.ApiEndpoint;
import edu.java.client.api.stackoverflow.dto.StackoverflowAnswersResponse;
import edu.java.client.api.stackoverflow.dto.StackoverflowCommentsResponse;
import edu.java.client.api.stackoverflow.dto.StackoverflowQuestionResponse;
import edu.java.response.LinkApiResponse;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackoverflowQuestionHandler extends StackoverflowLinkHandler {
    private static final Pattern QUESTION_URL_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    @Override
    protected Pattern getUrlPattern() {
        return QUESTION_URL_PATTERN;
    }

    @Override
    public ApiEndpoint<? extends LinkApiResponse> retrieveInfoEndpoint(URI url) {
        Matcher matcher = QUESTION_URL_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);

        return ApiEndpoint
            .callTo("/questions/%s?site=stackoverflow".formatted(questionId))
            .andReturn(StackoverflowQuestionResponse.class);
    }

    @Override
    public List<ApiEndpoint<? extends LinkUpdateResponse>> retrieveUpdatesEndpoints(URI url, OffsetDateTime fromDate) {
        Matcher matcher = QUESTION_URL_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);

        return List.of(
            ApiEndpoint
                .callTo("/questions/%s/answers?fromDate=%d&filter=withbody&site=stackoverflow"
                    .formatted(questionId, fromDate.toEpochSecond())
                )
                .andReturn(StackoverflowAnswersResponse.class),
            ApiEndpoint
                .callTo("/questions/%s/comments?fromDate=%d&filter=withbody&site=stackoverflow"
                    .formatted(questionId, fromDate.toEpochSecond())
                )
                .andReturn(StackoverflowCommentsResponse.class)
        );
    }
}
