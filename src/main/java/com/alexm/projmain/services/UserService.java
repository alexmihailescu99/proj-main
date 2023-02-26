package com.alexm.projmain.services;

import com.alexm.projmain.utils.HttpClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class UserService {
    private final HttpClientHelper httpClient;

    @Autowired
    public UserService(HttpClientHelper httpClient) {
        this.httpClient = httpClient;
    }

    public String getUser() {
        try {
            return httpClient.doGet("https://postman-echo.com/get").body();
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
