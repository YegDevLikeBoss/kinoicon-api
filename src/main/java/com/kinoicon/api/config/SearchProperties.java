package com.kinoicon.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.search")
@Data
public class SearchProperties {
    private double similarityThreshold;
    private int limit;
}
