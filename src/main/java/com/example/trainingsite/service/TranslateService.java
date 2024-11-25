package com.example.trainingsite.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TranslateService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.translate.url}")
    private String apiTranslateUrl;
    @Value("${api.translate.key}")
    private String apiTranslateKey;
    @Value("${api.translate.host}")
    private String apiTranslateHost;

    public TranslateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> translate(List<String> text, String source, String target) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiTranslateKey);
        headers.set("x-rapidapi-host", apiTranslateHost);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request body as a map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("q", text);
        requestBody.put("source", source);
        requestBody.put("target", target);

        // Convert the map to JSON
        String jsonRequest;
        try {
            jsonRequest = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request body to JSON", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        try {
            // Make the API request
            ResponseEntity<String> response = restTemplate.exchange(apiTranslateUrl, HttpMethod.POST, entity, String.class);

            // Parse the response to extract the translated text
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode translatedTextNode = jsonNode.path("data").path("translations").path("translatedText");

            List<String> translations = new ArrayList<>();
            if (translatedTextNode.isArray()) {
                for (JsonNode textNode : translatedTextNode) {
                    translations.add(textNode.asText());
                }
            }
            return translations;
        } catch (HttpStatusCodeException e) {
            // Handle HTTP errors
            throw new RuntimeException("Translation API request failed: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // Handle general errors
            throw new RuntimeException("Unexpected error while translating text", e);
        }
    }

}
