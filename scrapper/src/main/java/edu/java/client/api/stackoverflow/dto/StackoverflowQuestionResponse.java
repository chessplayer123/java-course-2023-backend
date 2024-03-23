package edu.java.client.api.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.response.LinkApiResponse;
import java.util.List;

public class StackoverflowQuestionResponse implements LinkApiResponse {
    private final String title;

    public StackoverflowQuestionResponse(
        @JsonProperty("items")
        List<QuestionInfo> info
    ) {
        title = info.getFirst().title;
    }

    @Override
    public String getSummary() {
        return "StackOverflow question: '%s'".formatted(title);
    }

    public record QuestionInfo(String title) {
    }
}
