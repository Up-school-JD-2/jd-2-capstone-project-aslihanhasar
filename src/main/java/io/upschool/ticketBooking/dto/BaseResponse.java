package io.upschool.ticketBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private int status;
    private boolean isSuccess;
    @Builder.Default
    private String errorMessage = "no message available";
    private T data;
}
