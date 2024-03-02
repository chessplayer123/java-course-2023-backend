package edu.java.client.stackoverflow;

import edu.java.link.LinkInfoSupplier;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackoverflowQuestionSubClient implements StackoverflowClient.SubClient {
    private static final Pattern QUESTION_URL_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    @Override
    public Pattern getUrlPattern() {
        return QUESTION_URL_PATTERN;
    }

    @Override
    public String convertUrlToApiPath(URL url) {
        Matcher matcher = QUESTION_URL_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);
        return "/questions/%s?site=stackoverflow".formatted(questionId);
    }

    @Override
    public Class<? extends LinkInfoSupplier> getInfoSupplierType() {
        return StackoverflowQuestionInfo.class;
    }
}
