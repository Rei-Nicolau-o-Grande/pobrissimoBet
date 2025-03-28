package bet.pobrissimo.exception;

import bet.pobrissimo.exception.exceptions.ValidationException;
import bet.pobrissimo.validators.exception.ValidationUserException;
import bet.pobrissimo.exception.dto.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiGlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorDto> handlerGenericException(HttpServletRequest request,
//                                                               RuntimeException ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(new ApiErrorDto(
//                        LocalDateTime.now(),
//                        request.getRequestURI(),
//                        request.getMethod(),
//                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                        ex.getMessage(),
//                        null,
//                        ex.getClass()
//                ));
//    }

    @ExceptionHandler({
            ValidationException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerException(HttpServletRequest request,
                                                        ValidationException ex) {

        return ResponseEntity
                .status(ex.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorDto(
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        request.getMethod(),
                        ex.getValue(),
                        ex.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerMethodArgumentNotValidException(HttpServletRequest request,
                                                                              MethodArgumentNotValidException ex,
                                                                              BindingResult result) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                "Campos inválidos"
        );
        apiErrorDto = apiErrorDto.addErrorField(result);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDto> handlerHttpMessageNotReadableException(HttpServletRequest request,
                                                                              HttpMessageNotReadableException ex) {
        ApiErrorDto errorDto = new ApiErrorDto(
                LocalDateTime.now(),
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getRootCause().getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ValidationUserException.class
    })
    public ResponseEntity<ApiErrorDto> handleValidationException(HttpServletRequest request,
                                                                 ValidationUserException ex) {
        String countValidationErrormessage = String.format(
                ex.getErrors().size() == 1 ? "Há %s campo inválido." : "Há %s campos inválidos.",
                ex.getErrors().size()
        );

        ApiErrorDto apiErrorDto = ApiErrorDto.withErrorFields(
                LocalDateTime.now(),
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                countValidationErrormessage,
                ex.getErrors(),
                null
        );

        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }
}
