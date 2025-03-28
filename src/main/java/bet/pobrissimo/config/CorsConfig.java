package bet.pobrissimo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig implements CorsConfigurationSource {

    @Value("${cors.allowed.origins.prod}")
    private String allowedOriginsProd;

    @Value("${cors.allowed.origins.test}")
    private String allowedOriginsTest;

    @Value("${cors.allowed.origins.dev}")
    private String allowedOriginsDev;

    @Value("${cors.allowed.mapping}")
    private String allowedMapping;

    @Value("${cors.allowed.methods}")
    private String[] allowedMethods;

    @Value("${cors.allowed.headers}")
    private String[] allowedHeaders;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOriginsProd, allowedOriginsTest, allowedOriginsDev));
        corsConfiguration.setAllowedMethods(Arrays.asList(allowedMethods));
        corsConfiguration.setAllowedHeaders(Arrays.asList(allowedHeaders));
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
