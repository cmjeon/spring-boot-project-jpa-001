package com.shop.demoShop.controller;

import com.shop.demoShop.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.demoShop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping(value = "/admin/item/new")
  public String itemForm(Model model){
    model.addAttribute("itemFormDto", new ItemFormDto());
    return "item/itemForm";
  }

  @PostMapping(value = "/admin/item/new")
  public String itemNew(@Valid ItemFormDto itemFormDto,
                        BindingResult bindingResult,
                        Model model,
                        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
    if (bindingResult.hasErrors()) { // 상품 등록 시 필수 값이 없다면 다시 상품 등록페이지로 이동
      return "item/itemForm";
    }
    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 상품 등록 시 첫번째 이미지가 없다면 에러메시지 + 상품 등록 페이지 전환
      model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
      return "item/itemForm";
    }
    try {
      itemService.saveItem(itemFormDto, itemImgFileList); // 상품 저장 로직을 호출
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품등록 중에러가 발생하였습니다.");
      return "item/itemForm";
    }
    return "redirect:/"; // 상품이 정상적으로 등록되었다면 메인 페이지로 이동
  }
}
