package edu.java.client.api;

import lombok.RequiredArgsConstructor;

public record ApiEndpoint<T>(
    String path,
    Class<T> responseType
) {
    public static ApiEndpointBuilder callTo(String urlPath) {
        return new ApiEndpointBuilder(urlPath);
    }

    @RequiredArgsConstructor
    public static class ApiEndpointBuilder {
        private final String endpointPath;

        public <T> ApiEndpoint<T> andReturn(Class<T> responseType) {
            return new ApiEndpoint<>(endpointPath, responseType);
        }
    }
}
