package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String itemName;

    private String itemDetail;

    private String sellStatCd;

    private Integer price;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;


}
