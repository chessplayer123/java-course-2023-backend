package edu.java.client.api;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record ApiEndpoint<T>(
    String path,
    Class<T> responseType,
    MultiValueMap<String, String> queryParams
) {
    public static ApiEndpointBuilder callTo(String urlPath, Object... params) {
        return new ApiEndpointBuilder(urlPath.formatted(params));
    }

    @RequiredArgsConstructor
    public static class ApiEndpointBuilder {
        private final String endpointPath;
        private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        public ApiEndpointBuilder withParam(String param, String value) {
            queryParams.putIfAbsent(param, new ArrayList<>());
            queryParams.get(param).add(value);

            return this;
        }

        public <T> ApiEndpoint<T> andReturn(Class<T> responseType) {
            return new ApiEndpoint<>(endpointPath, responseType, queryParams);
        }
    }
}
