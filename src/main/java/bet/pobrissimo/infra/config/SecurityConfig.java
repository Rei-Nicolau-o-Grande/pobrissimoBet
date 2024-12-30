package bet.pobrissimo.infra.config;

import bet.pobrissimo.infra.exception.CustomAccessDeniedHandler;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;

    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET, "/teste/deploy").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/token/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                    .requestMatchers(HttpMethod.GET, VALIDATION_ROUTES).permitAll()
                    .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                    .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfig))
            .oauth2ResourceServer(
                    oauth2 -> oauth2.jwt(jwt -> jwt
                            .jwtAuthenticationConverter(jwtMyAuthenticationConverter())))
            .sessionManagement(
                    session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(
                    exceptionHandling -> exceptionHandling
                            .accessDeniedHandler(new CustomAccessDeniedHandler()))
        ;

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtMyAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-park.html", "/docs-park/**",
            "/v3/api-docs/**",
            "/swagger-ui-configuration/**",
            "/swagger-ui-user.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**",
            "/docs/**",
    };

    private static final String[] VALIDATION_ROUTES = {
            "/api/v1/validation/username/**",
            "/api/v1/validation/email/**"
    };

}
