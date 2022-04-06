package com.shop.demoShop.dto;

import com.shop.demoShop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {
  public OrderItemDto(OrderItem orderItem, String imgUrl){
    this.itemNm = orderItem.getItem().getItemNm();
    this.count = orderItem.getCount();
    this.orderPrice = orderItem.getOrderPrice();
    this.imgUrl = imgUrl;
  }

  private String itemNm;
  private int count;
  private int orderPrice;
  private String imgUrl;
}
