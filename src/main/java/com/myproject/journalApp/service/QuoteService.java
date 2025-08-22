package com.myproject.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.journalApp.api.response.QuoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class QuoteService {

    @Value("${quote.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();  // Here Object Mapper has been used instead of
    // Deserialisation

    public QuoteResponse getQuote() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey); // key-value pair
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.api-ninjas.com/v1/quotes",
                HttpMethod.GET,
                entity,
                String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch quote data. Status code: " + response.getStatusCode());
        }
        // The process of converting JSON Response into corresponding Java Objects is known as Deserialisation.
        String responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Quote response body is empty");
        }

        try {
            // The API returns a JSON array, so parse as array and return the first element
            QuoteResponse[] quotes = objectMapper.readValue(responseBody, QuoteResponse[].class);
            if (quotes.length == 0) {
                throw new RuntimeException("No quotes found in response");
            }
            return quotes[0];
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse quote response", e);
        }

    }
}