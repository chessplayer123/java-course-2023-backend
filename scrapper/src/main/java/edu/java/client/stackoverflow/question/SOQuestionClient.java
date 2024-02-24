package edu.java.client.stackoverflow.question;

import edu.java.client.Client;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SOQuestionClient extends Client<SOQuestion> {
    private static final Pattern QUESTION_URL_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public SOQuestionClient(String baseUrl) {
        super(baseUrl);
    }

    public SOQuestionClient() {
        super("https://api.stackexchange.com/2.3");
    }

    @Override
    @Nullable
    public SOQuestion fetch(URL url) {
        Matcher matcher = QUESTION_URL_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);
        return sendRequest("/questions/%s?site=stackoverflow".formatted(questionId), SOQuestion.class);
    }

    @Override
    public boolean supports(URL url) {
        return QUESTION_URL_PATTERN.matcher(url.toString()).matches();
    }
}
