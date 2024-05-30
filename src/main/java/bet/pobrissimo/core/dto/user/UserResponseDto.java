package bet.pobrissimo.core.dto.user;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(UUID id, String username, String email, Set<String> roles) {
}
