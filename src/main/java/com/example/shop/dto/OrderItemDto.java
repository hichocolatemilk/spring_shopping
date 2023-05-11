package com.example.shop.dto;

import com.example.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemName;

    private String imgUrl;

    private int count;

    private int orderPrice;


    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
