package io.upschool.ticketBooking.exception;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.service.BaseResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {
    private final BaseResponseService baseResponseService;


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                   WebRequest request) {
        final var errorMessage =
                MessageFormat.format("No handler found for {0} {1} ", ex.getHttpMethod(), ex.getRequestURL());
        System.out.println(errorMessage);
        var response = BaseResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .isSuccess(false)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(final Exception exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage() +
                webRequest.getHeader(" client-type "));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AirlineValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleAirlineValidationException(
            final AirlineValidationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());

    }

    @ExceptionHandler(AirlineAlreadySaveException.class)
    public ResponseEntity<BaseResponse<?>> handleAirlineAlreadySaveException(
            final AirlineAlreadySaveException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleAirlineNotFoundException(
            final AirlineNotFoundException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AirportValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleAirportValidationException(
            final AirportValidationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AirportAlreadySaveException.class)
    public ResponseEntity<BaseResponse<?>> handleAirportAlreadySaveException(
            final AirportAlreadySaveException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleAirportNotFoundException(
            final AirportNotFoundException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RouteValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleFlightRouteValidationException(
            final RouteValidationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RouteAlreadySaveException.class)
    public ResponseEntity<BaseResponse<?>> handleRouteAlreadySaveException(
            final RouteAlreadySaveException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleRouteNotFoundException(
            final RouteNotFoundException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(FlightValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleFlightValidationException(
            final FlightValidationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());

    }

    @ExceptionHandler(FlightAlreadySaveException.class)
    public ResponseEntity<BaseResponse<?>> handleFlightAlreadySaveException(
            final FlightAlreadySaveException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleFlightNotFoundException(
            final FlightNotFoundException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(InvalidDateTimeFormatException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidDateTimeFormatException(
            final InvalidDateTimeFormatException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidTicketClassException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTicketClassException(
            final InvalidTicketClassException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotAvailableSeatException.class)
    public ResponseEntity<BaseResponse<?>> handleNotAvailableSeatException(
            final NotAvailableSeatException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<BaseResponse<?>> handlePaymentFailedException(
            final PaymentFailedException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidCreditCartNumberException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidCreditCartNumberException(
            final InvalidCreditCartNumberException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TicketValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleTicketValidationException(
            final TicketValidationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleTicketNotFoundException(
            final TicketNotFoundException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AlreadyCheckedInException.class)
    public ResponseEntity<BaseResponse<?>> handleAlreadyCheckedInException(
            final AlreadyCheckedInException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(TicketOperationException.class)
    public ResponseEntity<BaseResponse<?>> handleTicketOperationException(
            final TicketOperationException exception, final WebRequest webRequest) {
        System.out.println("Error acquired " + exception.getMessage());
        System.out.println(webRequest.toString());
        return baseResponseService.createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}




