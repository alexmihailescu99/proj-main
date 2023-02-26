package com.alexm.projmain.services;

import com.alexm.projmain.utils.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    private final HttpClientHelper httpClient;

    @Autowired
    public UserService(HttpClientHelper httpClient) {
        this.httpClient = httpClient;
    }

    /**
     *
     * @return a string
     */
    public String getUser(String uriAsString) {
        log.info("Trying to get user");
        try {
            return httpClient.doGet("https://postman-echo.com/get").body();
        } catch (IOException | InterruptedException e) {
            log.warn("Error sending GET request to {}.", uriAsString);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public String postUser(String uriAsString, String body) {
        try {
            return httpClient.doPost(uriAsString, body).body();
        } catch (IOException | InterruptedException e) {
            log.warn("Error sending POST request to {}.", uriAsString);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
