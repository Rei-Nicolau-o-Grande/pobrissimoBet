package bet.pobrissimo.core.dtos.user;

import java.util.UUID;

public record MeResponseDto(
        UUID id,
        String username,
        String email
) {
}
