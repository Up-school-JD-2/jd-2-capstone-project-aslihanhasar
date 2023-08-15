package io.upschool.ticketBooking.service;

import io.upschool.ticketBooking.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The BaseResponseService class provides utility methods for creating standardized
 * response entities with BaseResponse structure.
 * It is designed to simplify the process of generating success and error responses.
 */
@Service
public class BaseResponseService {

    /**
     * Creates a ResponseEntity containing a success response with data and a given HTTP status.
     *
     * @param status The HTTP status for the response.
     * @param data   The data to be included in the response.
     * @param <T>    The type of data to be included in the response.
     * @return A ResponseEntity containing the success response.
     */
    public <T> ResponseEntity<BaseResponse<T>> createSuccessResponse(HttpStatus status, T data) {
        var response = BaseResponse.<T>builder()
                .status(status.value())
                .data(data)
                .isSuccess(true)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Creates a ResponseEntity containing a success response with a list of data.
     *
     * @param data The list of data to be included in the response.
     * @param <T>  The type of data in the list.
     * @return A ResponseEntity containing the success response.
     */

    public <T> ResponseEntity<BaseResponse<List<T>>> createSuccessResponseList(List<T> data) {
        BaseResponse<List<T>> response = BaseResponse.<List<T>>builder()
                .status(HttpStatus.OK.value())
                .isSuccess(true)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a ResponseEntity containing an error response with a given HTTP status and error message.
     *
     * @param status The HTTP status for the response.
     * @param error  The error message to be included in the response.
     * @return A ResponseEntity containing the error response.
     */
    public ResponseEntity<BaseResponse<?>> createErrorResponse(HttpStatus status, String error) {
        var response = BaseResponse.builder()
                .status(status.value())
                .errorMessage(error)
                .isSuccess(false)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
