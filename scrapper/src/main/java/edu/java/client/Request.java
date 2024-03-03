package edu.java.client;

import edu.java.response.Response;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public class Request<T extends Response> {
    private final HttpMethod method;
    private final Class<T> responseClass;
    private final String endpoint;

    private Request(String endpoint, HttpMethod method, Class<T> responseTypeClass) {
        this.endpoint = endpoint;
        this.method = method;
        this.responseClass = responseTypeClass;
    }

    public static <T extends Response> Request<T> get(
        String endpoint,
        Class<T> responseTypeClass
    ) {
        return new Request<>(endpoint, HttpMethod.GET, responseTypeClass);
    }

    public static <T extends Response> Request<T> post(
        String endpoint,
        Class<T> responseClass
    ) {
        return new Request<>(endpoint, HttpMethod.POST, responseClass);
    }

    public static <T extends Response> Request<T> delete(
        String endpoint,
        Class<T> responseClass
    ) {
        return new Request<>(endpoint, HttpMethod.DELETE, responseClass);
    }
}
