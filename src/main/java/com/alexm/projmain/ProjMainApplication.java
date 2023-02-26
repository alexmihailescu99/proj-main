package com.alexm.projmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
public class ProjMainApplication {
    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjMainApplication.class, args);
    }

}
