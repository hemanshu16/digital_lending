package com.digitallending.breservice.exceptionhandler;

import com.digitallending.breservice.exception.AlreadyExistException;
import com.digitallending.breservice.exception.EmptyPayloadException;
import com.digitallending.breservice.exception.ExternalServiceException;
import com.digitallending.breservice.exception.InvalidParameterException;
import com.digitallending.breservice.exception.InvalidRangeException;
import com.digitallending.breservice.exception.InvalidValueException;
import com.digitallending.breservice.exception.InvalidWeightException;
import com.digitallending.breservice.exception.NotAllowedException;
import com.digitallending.breservice.exception.NotFoundException;
import com.digitallending.breservice.model.dto.apiresponse.APIResponse;
import com.digitallending.breservice.model.dto.apiresponse.BREError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String MESSAGE = "message";
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<APIResponse<BREError>> handleNotFoundException(NotFoundException e, WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidWeightException.class)
    public ResponseEntity<APIResponse<BREError>> handleInvalidWeightException(InvalidWeightException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<BREError>> handleValidationException(MethodArgumentNotValidException e,WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidValueException.class)
    public ResponseEntity<APIResponse<BREError>> handleInvalidValueException(InvalidValueException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidParameterException.class)
    public ResponseEntity<APIResponse<BREError>> handleInvalidParameterException(InvalidParameterException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<APIResponse<BREError>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e,WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Already Exists param weights entry for requested loan ProductId");
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<APIResponse<BREError>> handleAlreadyExistException(AlreadyExistException e, WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotAllowedException.class)
    public ResponseEntity<APIResponse<BREError>> handleNotAllowedException(NotAllowedException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExternalServiceException.class)
    public ResponseEntity<APIResponse<BREError>> handleExternalServiceException(ExternalServiceException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmptyPayloadException.class)
    public ResponseEntity<APIResponse<BREError>> handleEmptyPayloadException(EmptyPayloadException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRangeException.class)
    public ResponseEntity<APIResponse<BREError>> handleInvalidRangeException(InvalidRangeException e,WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, e.getMessage());
        BREError errorDetails = BREError.builder().error(errors).uri(request.getDescription(false)).build();
        APIResponse<BREError> apiResponse = APIResponse.<BREError>builder().payload(errorDetails).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
