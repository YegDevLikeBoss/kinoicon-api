package com.kinoicon.api.service.impl;

import com.kinoicon.api.service.KpRatingClient;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Ports the original get_kp_rating() helper: fetches https://rating.kinopoisk.ru/{kp_id}.xml
 * and parses the first two elements as KP and IMDB rating/vote-count. Any non-200 response
 * or parsing issue is swallowed and treated as "no rating available", matching the Flask
 * behaviour of silently skipping the rating update rather than failing the PUT request.
 */
@Component
public class KpRatingClientImpl implements KpRatingClient {

    private static final String KP_URI = "https://rating.kinopoisk.ru/%s.xml";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Override
    public Optional<KpRating> fetchRating(String kpId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(KP_URI, kpId)))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(response.body().getBytes()));

            Element root = document.getDocumentElement();
            Element kpElement = (Element) root.getElementsByTagName("*").item(0);
            Element imdbElement = (Element) root.getElementsByTagName("*").item(1);

            String kpRating = kpElement.getTextContent();
            int kpVoteCount = Integer.parseInt(kpElement.getAttribute("num_vote"));
            String imdbRating = imdbElement.getTextContent();
            int imdbVoteCount = Integer.parseInt(imdbElement.getAttribute("num_vote"));

            return Optional.of(new KpRating(kpRating, kpVoteCount, imdbRating, imdbVoteCount));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
