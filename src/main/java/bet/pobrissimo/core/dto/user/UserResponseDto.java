package bet.pobrissimo.core.dto.user;

import bet.pobrissimo.core.dto.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.model.Wallet;
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
