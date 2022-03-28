package com.shop.demoShop.service;

import com.shop.demoShop.entity.ItemImg;
import com.shop.demoShop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

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

  public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
    if(!itemImgFile.isEmpty()){                                         //상품 이미지를 수정한 경우 상품 이미지를 업데이트
      ItemImg savedItemImg = itemImgRepository.findById(itemImgId)    //기존에 저장했던 엔티티 조회
          .orElseThrow(EntityNotFoundException::new);

      //기존 이미지가 있으면 삭제
      if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
        fileService.deleteFile(itemImgLocation+"/"+
            savedItemImg.getImgName());
      }

      // 업데이트한 상품 이미지 파일 업로드
      String oriImgName = itemImgFile.getOriginalFilename();
      String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      String imgUrl = "/images/item/" + imgName;

      //변경된 상품 이미지 정보를 세팅.
      // 여기서 중요한 점은 상품등록 때처럼 itemImgRepository.save() 로직을 호출하지 않는다.
      // savedItemlmg 엔티티는 현재 영속 상태이므로 데이터를 변경하는 것만으로 변경 감지 기능이 동작하여, 트랜잭션이 끝날 때 update 쿼리가 실행됨.
      // 여기서 중요한 것은 엔티티가 영속 상태여야 한다는 것
      savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
    }
  }
}
