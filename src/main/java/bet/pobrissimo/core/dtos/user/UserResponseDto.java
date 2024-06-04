package bet.pobrissimo.core.dtos.user;

import bet.pobrissimo.core.dtos.wallet.MyWalletResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
        Instant createdAt,

        Boolean isActive,

        MyWalletResponseDto wallet,

        Set<String> roles) {
}
