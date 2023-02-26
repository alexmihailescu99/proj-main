package com.alexm.projmain.utils;


import com.alexm.projmain.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

@Component // this allows our class to be injected by Spring wherever we need it
public class HttpClientHelper {
    Logger log = LoggerFactory.getLogger(UserService.class);

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    @Autowired
    public HttpClientHelper(HttpClient client,
                            ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    /**
     * Executes a GET request on a given URI.
     *
     * @param uri the URI on which to execute the GET request
     * @return the response of the GET request
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public HttpResponse<String> doGet(String uri) throws IOException, InterruptedException {
        log.info("Trying to execute a GET request on {}", uri);
        var request = createGetRequest(uri);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> doPost(String uri, Object body) throws IOException, InterruptedException {
        log.info("Trying to execute a GET request on {}", uri);
        try {
            var bodyAsString = objectMapper.writeValueAsString(body);
            var request = createPostRequest(uri, bodyAsString);
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (JsonProcessingException e) {
            log.warn("Error processing the JSON body. The POST request will not continue");
            return null;
        }
    }

    private HttpRequest createGetRequest(String uri) {
        log.info("Received a request for a GET request");
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private HttpRequest createPostRequest(String uri, String body) {
        log.info("Received a request for a POST request");
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .version(HttpClient.Version.HTTP_2)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private HttpRequest createPostRequest(String uri, File body) {
        log.info("Received a request for a POST request");
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .version(HttpClient.Version.HTTP_2)
                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(body.getPath())))
                    .build();
        } catch (URISyntaxException e) {
            log.warn("Malformed URI syntax: {}", uri);
            return null;
        } catch (FileNotFoundException e) {
            log.warn("Could not find file: {}", body.getPath());
            return null;
        }
    }

    private HttpRequest createPutRequest(String uri) {
        log.info("Received a request for a PUT request");
        return createRequest(uri, HttpConstants.PUT);
    }

    private HttpRequest createDeleteRequest(String uri) {
        log.info("Received a request for a DELETE request");
        return createRequest(uri, HttpConstants.DELETE);
    }

    private HttpRequest createRequest(String uri, String requestType) {
        try {
            return switch (requestType) {
                case "POST" -> HttpRequest.newBuilder()
                        .uri(new URI(uri))
                        .version(HttpClient.Version.HTTP_2)
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                case "PUT" -> HttpRequest.newBuilder()
                        .uri(new URI(uri))
                        .version(HttpClient.Version.HTTP_2)
                        .PUT(HttpRequest.BodyPublishers.noBody())
                        .build();
                case "DELETE" -> HttpRequest.newBuilder()
                        .uri(new URI(uri))
                        .version(HttpClient.Version.HTTP_2)
                        .DELETE()
                        .build();
                default -> null;
            };
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
