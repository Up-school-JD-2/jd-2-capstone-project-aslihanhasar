package io.upschool.ticketBooking.service;

import io.upschool.ticketBooking.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseResponseService {

    public <T> ResponseEntity<BaseResponse<T>> createSuccessResponse(HttpStatus status, T data) {
        var response = BaseResponse.<T>builder()
                .status(status.value())
                .data(data)
                .isSuccess(true)
                .build();
        return ResponseEntity.status(status).body(response);
    }


    public <T> ResponseEntity<BaseResponse<List<T>>> createSuccessResponseList(List<T> data) {
        BaseResponse<List<T>> response = BaseResponse.<List<T>>builder()
                .status(HttpStatus.OK.value())
                .isSuccess(true)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<BaseResponse<?>> createErrorResponse(HttpStatus status, String error) {
        var response = BaseResponse.builder()
                .status(status.value())
                .errorMessage(error)
                .isSuccess(false)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
