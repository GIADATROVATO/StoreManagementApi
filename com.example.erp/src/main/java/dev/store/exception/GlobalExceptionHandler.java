package dev.store.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.store.payload.ApiError;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ClienteNotFoundException.class)
	public ResponseEntity<ApiError> handleCliente(ClienteNotFoundException e,HttpServletRequest request) {
		ApiError a= new ApiError( LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(a,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(OrdineNotFoundException.class)
	public ResponseEntity<ApiError> handleOrdine(OrdineNotFoundException e, HttpServletRequest request){
		ApiError a= new ApiError( LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(a,HttpStatus.NOT_FOUND );	
	}
	@ExceptionHandler(OrdineNonModificabileException.class)
	public ResponseEntity<ApiError> handleIncoerenzaOrdine(OrdineNonModificabileException e, HttpServletRequest request){
		ApiError a= new ApiError( LocalDateTime.now(),
				HttpStatus.NOT_ACCEPTABLE.value(),
				e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(a,HttpStatus.NOT_ACCEPTABLE);	
	
	}
	@ExceptionHandler(ArticoloNonDisponibileException.class)
	public ResponseEntity<ApiError> handleIncoerenzaOrdine(ArticoloNonDisponibileException e, HttpServletRequest request){
		ApiError a= new ApiError( LocalDateTime.now(),
				HttpStatus.NOT_ACCEPTABLE.value(),
				e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(a,HttpStatus.NOT_ACCEPTABLE);	
	
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex,HttpServletRequest request ) {
    	ApiError error= new ApiError(
    			LocalDateTime.now(),
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                request.getRequestURI());
    	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
