package com.shop.demoShop.service;

import com.shop.demoShop.entity.ItemImg;
import com.shop.demoShop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

  @Value("${itemImgLocation}") // application.properties 의 itemImgLocation 사용
  private String itemImgLocation;
  private final ItemImgRepository itemImgRepository;
  private final FileService fileService;

  public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
    System.out.println("###" + itemImgLocation);
    String oriImgName = itemImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";
    //파일 업로드
    if(!StringUtils.isEmpty(oriImgName)){
      imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 파일저장
      imgUrl = "/images/item/" + imgName; // 저장한 상품 이미지를 불러올 경로를 설정. WebMvcConfig 와 application.properties 에 설정한 uploadPath 참조하여 설정
    }

    //상품 이미지 정보 저장
    itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 아이템이미지 객체에 수정
    itemImgRepository.save(itemImg); // 아이템이미지 객체 저장
  }
}
