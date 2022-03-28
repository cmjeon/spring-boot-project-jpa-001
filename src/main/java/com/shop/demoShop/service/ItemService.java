package com.shop.demoShop.service;


import com.shop.demoShop.dto.ItemFormDto;
import com.shop.demoShop.dto.ItemImgDto;
import com.shop.demoShop.entity.Item;
import com.shop.demoShop.entity.ItemImg;
import com.shop.demoShop.repository.ItemImgRepository;
import com.shop.demoShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem(); // item 객체 생성
        itemRepository.save(item); // 상품 데이터 저장

        // 이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepimgYn("Y"); // 첫번째 이미지는 대표이미지로 세팅
            else
                itemImg.setRepimgYn("N"); // 나머지 이미지는 대표이미지 아님
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){ //상품 데이터를 읽어오는 트랜잭션을 읽기 전용을 설정
        //이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있다.

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 등록순으로 하려고 아이디 오름차순으로 조회
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {                   // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)             //상품 엔티티 조회, 없으면 EntityNotFoundException 발생
            .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
            .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                itemImgFileList.get(i));
        }

        return item.getId();
    }

}
