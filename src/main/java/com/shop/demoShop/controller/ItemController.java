package com.shop.demoShop.controller;

import com.shop.demoShop.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ItemController {
  @GetMapping(value = "/admin/item/new")
  public String itemForm(Model model){
    model.addAttribute("itemFormDto", new ItemFormDto());
    return "item/itemForm";
  }
}
