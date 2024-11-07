package vn.job.exception;


import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // handle validations
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        errorResponse.setMessage(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(f -> f.getDefaultMessage())
                .collect(Collectors.toList());
        errorResponse.setMessage(String.join(", ", errorMessages));
        return errorResponse;
    }


    // handle convert value
    @ExceptionHandler({MethodArgumentTypeMismatchException.class })
    public ErrorResponse handleInternalServerException(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError("Parameter invalid");
        if(e instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setMessage("Failed to convert value of type");
        }
        return errorResponse;
    }

    // handle error link
    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse handleNotFoundException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage("URL may not exist");
        return errorResponse;
    }

    // handle error convert json (enum)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError("Invalid input format");

        String message = "";
        if (e.getCause() != null) {
            message = e.getCause().getMessage();
        }
        errorResponse.setMessage("JSON parse error: " + message);

        return errorResponse;
    }



    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse handleMaxUploadException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Maximum upload size exceeded ");
        return errorResponse;
    }


}
