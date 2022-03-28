package com.shop.demoShop.dto;

import com.shop.demoShop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

  /*
  searchDateType
  * - all:상품등록일전체
  * - 1d: 최근하루동안등록된상품
  * - 1w: 최근일주일동안등록된상품
  * - 1m: 최근한달동안등록된상품
  * - 6m: 최근6개월동안등록된상품
  */

  private String searchDateType; //현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회
  private ItemSellStatus searchSellStatus;
  private String searchBy;
  private String searchQuery = "";
}