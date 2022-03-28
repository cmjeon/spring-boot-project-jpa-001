package com.shop.demoShop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.demoShop.constant.ItemSellStatus;
import com.shop.demoShop.dto.ItemSearchDto;
import com.shop.demoShop.entity.Item;
import com.shop.demoShop.entity.QItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{  //ItemRepositoryCustom 상속
  private JPAQueryFactory queryFactory;

  public ItemRepositoryCustomImpl(EntityManager em){
    this.queryFactory = new JPAQueryFactory(em);
  }

  private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
    return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
  }

  //searchDateType 값에 따라서 dateTime 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회
  private BooleanExpression regDtsAfter(String searchDateType){

    LocalDateTime dateTime = LocalDateTime.now();

    if(StringUtils.equals("all", searchDateType) || searchDateType == null){
      return null;
    } else if(StringUtils.equals("1d", searchDateType)){
      dateTime = dateTime.minusDays(1);
    } else if(StringUtils.equals("1w", searchDateType)){
      dateTime = dateTime.minusWeeks(1);
    } else if(StringUtils.equals("1m", searchDateType)){
      dateTime = dateTime.minusMonths(1);
    } else if(StringUtils.equals("6m", searchDateType)){
      dateTime = dateTime.minusMonths(6);
    }

    return QItem.item.regTime.after(dateTime);
  }

  private BooleanExpression searchByLike(String searchBy, String searchQuery){

    if(StringUtils.equals("itemNm", searchBy)){
      return QItem.item.itemNm.like("%" + searchQuery + "%");
    } else if(StringUtils.equals("createdBy", searchBy)){
      return QItem.item.createdBy.like("%" + searchQuery + "%");
    }

    return null;
  }

  @Override
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

    QueryResults<Item> results = queryFactory
        .selectFrom(QItem.item)
        .where(regDtsAfter(itemSearchDto.getSearchDateType()),
            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
            searchByLike(itemSearchDto.getSearchBy(),
                itemSearchDto.getSearchQuery()))
        .orderBy(QItem.item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<Item> content = results.getResults();
    long total = results.getTotal();

    return new PageImpl<>(content, pageable, total);
  }

}