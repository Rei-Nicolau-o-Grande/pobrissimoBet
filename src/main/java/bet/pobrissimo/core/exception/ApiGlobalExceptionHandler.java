package bet.pobrissimo.core.exception;

import bet.pobrissimo.core.validators.exception.ValidationException;
import bet.pobrissimo.core.exception.dto.ApiErrorDto;
import bet.pobrissimo.infra.exception.*;
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
            AccessDeniedException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerForbiddenException(HttpServletRequest request,
                                                                   RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorDto(
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        request.getMethod(),
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            BadCredentialsException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerUnauthorizedException(HttpServletRequest request,
                                                                     RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorDto(
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        request.getMethod(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            InvalidUUIDException.class,
            EntityNotFoundException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerNotFoundException(HttpServletRequest request,
                                                                RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorDto(
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        request.getMethod(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            TransactionWithDrawException.class,
            TransactionTypeDoesNotExistException.class,
    })
    public ResponseEntity<ApiErrorDto> handlerBadRequestException(HttpServletRequest request,
                                                                  RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorDto(
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        request.getMethod(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
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
        ValidationException.class
    })
    public ResponseEntity<ApiErrorDto> handleValidationException(HttpServletRequest request,
                                                                 ValidationException ex) {
        ApiErrorDto apiErrorDto = ApiErrorDto.withErrorFields(
                LocalDateTime.now(),
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                "Campos inválidos",
                ex.getErrors(),
                null
        );

        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }
}
