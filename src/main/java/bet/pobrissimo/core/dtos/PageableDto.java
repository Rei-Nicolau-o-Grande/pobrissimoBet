package bet.pobrissimo.core.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageableDto(
        List<?> content,

        @JsonProperty("page")
        int number,

        int size,

        @JsonProperty("pageElements")
        int numberOfElements,

        int totalPages,

        long totalElements,

        String sort
) {
}
