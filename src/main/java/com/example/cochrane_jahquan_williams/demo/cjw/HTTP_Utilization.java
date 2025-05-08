package com.example.cochrane_jahquan_williams.demo.cjw;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTP_Utilization {

    private final String INIT_URL = "https://www.google.com/" + "" /*edit this url extension as needed*/;
    private final HttpClient client;

    public HTTP_Utilization() {
        client = HttpClient.newHttpClient(); // this is giving instance of the client
    }

    public String findAll() throws IOException, InterruptedException
        /*  */
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(INIT_URL))
                .GET()
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
