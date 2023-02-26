package com.alexm.projmain.utils;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component // this allows our class to be injected by Spring wherever we need it
public record HttpClientHelper(HttpClient client) {
    public HttpResponse<String> doGet(String uri) throws IOException, InterruptedException {
        var request = createGetRequest(uri);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest createGetRequest(String uri) {
        return createRequest(uri, HttpConstants.GET);
    }

    private HttpRequest createRequest(String uri, String requestType) {
        try {
            return switch (requestType) {
                case "GET" -> HttpRequest.newBuilder()
                        .uri(new URI(uri))
                        .version(HttpClient.Version.HTTP_2)
                        .GET()
                        .build();
                default -> null;
            };
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
