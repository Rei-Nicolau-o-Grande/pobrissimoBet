package bet.pobrissimo.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorDto(

        @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
        LocalDateTime timestamp,

        String path,

        String method,

        Integer status,

        String error,

        String message,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> fields
) {
    public ApiErrorDto(LocalDateTime timestamp, String path, String method, Integer status, String error,
                       String message) {
        this(timestamp, path, method, status, error, message, null);
    }

    public ApiErrorDto(LocalDateTime timestamp, String path, String method, Integer status, String error,
                       String message, Map<String, String> fields) {
        this.timestamp = timestamp;
        this.path = path;
        this.method = method;
        this.status = status;
        this.error = error;
        this.message = message;
        this.fields = fields;
    }
}