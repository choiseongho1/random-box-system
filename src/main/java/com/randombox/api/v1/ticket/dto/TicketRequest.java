package com.randombox.api.v1.ticket.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TicketRequest {

    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotBlank(message = "티켓 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "티켓 설명은 필수입니다.")
        private String description;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        private Integer price;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        private Integer quantity;

        @NotNull(message = "이벤트 일시는 필수입니다.")
        @Future(message = "이벤트 일시는 미래 시간이어야 합니다.")
        private LocalDateTime eventDateTime;

        @NotBlank(message = "장소는 필수입니다.")
        private String venue;

        @NotNull(message = "판매 시작 시간은 필수입니다.")
        @Future(message = "판매 시작 시간은 미래 시간이어야 합니다.")
        private LocalDateTime salesStartTime;

        @NotNull(message = "판매 종료 시간은 필수입니다.")
        @Future(message = "판매 종료 시간은 미래 시간이어야 합니다.")
        private LocalDateTime salesEndTime;
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "티켓 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "티켓 설명은 필수입니다.")
        private String description;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        private Integer price;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        private Integer quantity;

        @NotNull(message = "이벤트 일시는 필수입니다.")
        @Future(message = "이벤트 일시는 미래 시간이어야 합니다.")
        private LocalDateTime eventDateTime;

        @NotBlank(message = "장소는 필수입니다.")
        private String venue;

        @NotNull(message = "판매 시작 시간은 필수입니다.")
        @Future(message = "판매 시작 시간은 미래 시간이어야 합니다.")
        private LocalDateTime salesStartTime;

        @NotNull(message = "판매 종료 시간은 필수입니다.")
        @Future(message = "판매 종료 시간은 미래 시간이어야 합니다.")
        private LocalDateTime salesEndTime;
    }
}
