package bet.pobrissimo.infra.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class AuthenticatedCurrentUser {

    private static Map<String, Object> extractClaim() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return ((Jwt) principal).getClaims();
    }

    public static UUID getUserId() {
        return UUID.fromString((String) extractClaim().get("sub"));
    }

    public static String getUserName() {
        return (String) extractClaim().get("username");
    }

    public static String getUserEmail() {
        return (String) extractClaim().get("email");
    }

    public static Set<String> getUserRoles() {
        List<String> rolesList = (List<String>) extractClaim().get("roles");
        return new HashSet<>(rolesList);
    }
}
