package com.shop.demoShop.repository;

import com.shop.demoShop.dto.ItemSearchDto;
import com.shop.demoShop.dto.MainItemDto;
import com.shop.demoShop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ItemRepositoryCustom {
  Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
  Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}