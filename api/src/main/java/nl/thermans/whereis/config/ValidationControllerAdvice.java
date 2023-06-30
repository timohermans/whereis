package nl.thermans.whereis.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
public class ValidationControllerAdvice extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;
    private final Locale locale;

    public ValidationControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
        locale = Locale.ENGLISH;
    }

    // Okay so I know I should not let the database throw an exception
    // but hey, we all need our time.
    // if this becomes a bottleneck I'll fix it by then
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabaseError(DataIntegrityViolationException ex, WebRequest request) throws Exception {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintException) {
            var exceptionMessage = constraintException.getMessage();
            Pattern pattern = Pattern.compile("insert into\\s*(\\w+)");
            Matcher matcher = pattern.matcher(exceptionMessage);

            if (exceptionMessage.contains("duplicate key") && matcher.find()) {
                var entity = matcher.group(1);
                var className = entity.substring(0, 1).toUpperCase() + entity.substring(1);
                var message = tryGetMessageFrom(className + ".Unique");
                return new ResponseEntity<>(new ValidationErrorEntityResponse(List.of(new ValidationError(entity, message))), HttpStatus.BAD_REQUEST);
            }

            return super.handleException(ex, request);
        }


        return super.handleException(ex, request);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleInvalidEntity(TransactionSystemException ex, WebRequest request) throws Exception {

        if (ex.getRootCause() instanceof ConstraintViolationException constraintViolationException) {
            var errors = constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(vio -> {
                        var constraint = vio.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
                        var entity = vio.getRootBeanClass().getSimpleName();
                        var property = vio.getPropertyPath().toString();
                        var messageCode = String.format("%s.%s.%s", entity, property, constraint);
                        var message = tryGetMessageFrom(messageCode);
                        return new ValidationError(property, message);
                    })
                    .toList();

            return new ResponseEntity<>(new ValidationErrorEntityResponse(errors), HttpStatus.BAD_REQUEST);
        }

        return super.handleException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var bindingResult = ex.getBindingResult();
        var model = ex.getObjectName();
        List<ValidationError> errorList = bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    var property = fieldError.getField();
                    var error = fieldError.getCode();
                    var errorCode = String.format("%s.%s.%s", model, property, error);
                    String message = tryGetMessageFrom(errorCode);
                    return new ValidationError(fieldError.getField(), message);
                })
                .toList();

        return handleExceptionInternal(ex, errorList, headers, status, request);
    }

    private String tryGetMessageFrom(String errorCode) {
        String message;
        try {
            message = messageSource.getMessage(errorCode, null, locale);
        } catch (NoSuchMessageException e) {
            message = errorCode;
        }
        return message;
    }
}
