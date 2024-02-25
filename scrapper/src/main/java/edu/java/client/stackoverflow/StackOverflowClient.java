package edu.java.client.stackoverflow;

import edu.java.client.Client;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowClient extends Client {
    private static final Pattern QUESTION_URL_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public StackOverflowClient(String baseUrl) {
        super(baseUrl);
    }

    public StackOverflowClient() {
        super("https://api.stackexchange.com/2.3");
    }

    @Override
    @Nullable
    public LinkInfoSupplier fetch(URL url) {
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
