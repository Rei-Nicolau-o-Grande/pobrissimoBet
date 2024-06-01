package bet.pobrissimo.core.dto.user;

import java.util.UUID;

public record MeResponseDto(
        UUID id,
        String username,
        String email
) {
}
