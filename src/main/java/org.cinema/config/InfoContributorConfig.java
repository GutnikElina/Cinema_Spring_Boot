package org.cinema.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@OpenAPIDefinition(
        info = @Info(title = "Cinema API", version = "1.0", description = "API documentation for Cinema Spring Boot")
)
public class InfoContributorConfig implements InfoContributor {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${application.version:1.0.0}")
    private String appVersion;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${omdb.api.url}")
    private String omdbApiUrl;

    @Override
    public void contribute(org.springframework.boot.actuate.info.Info.Builder builder) {
        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("name", appName);
        appDetails.put("version", appVersion);

        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("url", databaseUrl);

        Map<String, Object> apiDetails = new HashMap<>();
        apiDetails.put("OMDB API", omdbApiUrl);

        builder.withDetail("app", appDetails)
                .withDetail("database", dbDetails)
                .withDetail("external_api", apiDetails);
    }
}