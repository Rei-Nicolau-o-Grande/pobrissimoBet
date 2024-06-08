package bet.pobrissimo.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.HashMap;
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
        Map<String, String> errorFields,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object stakeTrace
) {
    public ApiErrorDto(LocalDateTime timestamp, String path, String method, Integer status, String error,
                       String message) {
        this(timestamp, path, method, status, error, message, null, null);
    }

    public ApiErrorDto(LocalDateTime timestamp, String path, String method, Integer status, String error,
                       String message, Map<String, String> errorFields, Object stakeTrace) {
        this.timestamp = timestamp;
        this.path = path;
        this.method = method;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorFields = errorFields != null ? errorFields : new HashMap<>();
        this.stakeTrace = stakeTrace;
    }

    public ApiErrorDto addErrorField(BindingResult result) {
        Map<String, String> errorFields = new HashMap<>(this.errorFields);
        result.getFieldErrors().forEach(fieldError ->
                errorFields.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ApiErrorDto(timestamp, path, method, status, error, message, errorFields, stakeTrace);
    }
}