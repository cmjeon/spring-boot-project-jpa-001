package com.shop.demoShop.repository;

import com.shop.demoShop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
  List<Item> findByItemNm(String itemNm);
  List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
  List<Item> findByPriceLessThan(Integer price);
  List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
  @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
  @Query(value="SELECT * FROM Item i WHERE i.item_detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery=true)
  List<Item> findByItemDetailByNative(@Param("itemDetail" ) String itemDetail);
}