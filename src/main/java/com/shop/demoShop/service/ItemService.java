package com.shop.demoShop.service;


import com.shop.demoShop.dto.ItemFormDto;
import com.shop.demoShop.entity.Item;
import com.shop.demoShop.entity.ItemImg;
import com.shop.demoShop.repository.ItemImgRepository;
import com.shop.demoShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor

public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

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

}
