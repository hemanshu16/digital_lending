package com.digitallending.loanservice.exceptionhandler;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.BankAccountNotFoundException;
import com.digitallending.loanservice.exception.DocumentTypeNotFoundException;
import com.digitallending.loanservice.exception.ExternalServiceException;
import com.digitallending.loanservice.exception.InvalidOtpException;
import com.digitallending.loanservice.exception.LoanApplicationNotFoundException;
import com.digitallending.loanservice.exception.LoanRequestNotFoundException;
import com.digitallending.loanservice.exception.LoanTypeNotFoundException;
import com.digitallending.loanservice.exception.PropertyTypeNotFoundException;
import com.digitallending.loanservice.exception.SignedDocumentNotFoundException;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.apiresponse.LoanServiceError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    static final String MESSAGE = "message";

    @ExceptionHandler({Exception.class,
            ExternalServiceException.class})
    public ResponseEntity<ApiResponse<LoanServiceError>> handleAllException(Exception ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        LoanServiceError errorDetails = LoanServiceError
                .builder()
                .error(errors)
                .uri(request.getDescription(false))
                .build();

        ApiResponse<LoanServiceError> apiResponse = ApiResponse
                .<LoanServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({LoanApplicationNotFoundException.class,
            DocumentTypeNotFoundException.class,
            LoanTypeNotFoundException.class,
            PropertyTypeNotFoundException.class,
            LoanRequestNotFoundException.class,
            PropertyTypeNotFoundException.class,
            BankAccountNotFoundException.class,
            SignedDocumentNotFoundException.class})
    public ResponseEntity<ApiResponse<LoanServiceError>> handleNotFoundException(Exception ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        LoanServiceError errorDetails = LoanServiceError
                .builder()
                .error(errors)
                .uri(request.getDescription(false))
                .build();
        ApiResponse<LoanServiceError> apiResponse = ApiResponse
                .<LoanServiceError>builder()

                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class,InvalidOtpException.class})
    public ResponseEntity<ApiResponse<LoanServiceError>> handleBadRequestException(Exception ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        LoanServiceError errorDetails = LoanServiceError
                .builder()
                .error(errors)
                .uri(request.getDescription(false))
                .build();

        ApiResponse<LoanServiceError> apiResponse = ApiResponse
                .<LoanServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        LoanServiceError errorDetails = LoanServiceError
                .builder()
                .error(errors)
                .uri(request.getDescription(false))
                .build();

        ApiResponse<LoanServiceError> apiResponse = ApiResponse
                .<LoanServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ApiResponse<LoanServiceError>> handleDataIntegrityViolationException(

            DataIntegrityViolationException ex, WebRequest request) {
        String errorMessage = "Data integrity violation.";
        logger.debug(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, errorMessage);
        LoanServiceError errorDetails = LoanServiceError
                .builder()
                .error(errors)
                .uri(request.getDescription(false))
                .build();
        ApiResponse<LoanServiceError> apiResponse = ApiResponse
                .<LoanServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }
}
