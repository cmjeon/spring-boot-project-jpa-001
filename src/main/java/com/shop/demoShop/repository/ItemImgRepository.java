package com.shop.demoShop.repository;

import com.shop.demoShop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

  List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}