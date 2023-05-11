package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 1개 이상")
    @Max(value = 999, message = "최대 주문 수량은 999")
    private int count;
}
