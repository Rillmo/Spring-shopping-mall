package shopping.mall.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shopping.mall.Form.ItemForm;
import shopping.mall.Form.MemberForm;
import shopping.mall.domain.Item;
import shopping.mall.service.ItemService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;


    // 상품 등록(관리자)
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "/items/createItemForm";
    }

    @PostMapping("/new")
    public String createItem(@Validated ItemForm itemForm, BindingResult result) {
        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        // Item 엔티티 생성
        Item item = new Item();
        item.setName(itemForm.getName());
        item.setStockQuantity(itemForm.getStockQuantity());
        item.setPrice(itemForm.getPrice());
        item.setDetail(itemForm.getDetail());
        item.setGrade(itemForm.getGrade());
        item.setRegistDate(LocalDate.now());
        item.setFinalUpdateDate(LocalDate.now());
        item.setViews(0);

        try {
            itemService.saveItem(item);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            result.rejectValue("name", "duplicateItemName", e.getMessage());
            return "items/createItemForm";
        }
        return "redirect:/items/list";
    }

    @GetMapping("/list")
    public String itemList(Model model) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/{id}/delete")
    public String itemDelete(@PathVariable("id") Long id) {
        Item item = itemService.findItemById(id);
        log.info(item.getName());
        Long deletedId = itemService.deleteItem(id);
        log.info(deletedId.toString());
        return "redirect:/items/list";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Item item = itemService.findItemById(id);
        ItemForm itemForm = new ItemForm();
        itemForm.setId(item.getId());
        itemForm.setName(item.getName());
        itemForm.setStockQuantity(item.getStockQuantity());
        itemForm.setPrice(item.getPrice());
        itemForm.setDetail(item.getDetail());

        model.addAttribute("itemForm", itemForm);
        return "items/updateItemForm";
    }

    @PostMapping("/{id}/update")
    public String updateItem(@ModelAttribute("itemForm") ItemForm itemForm) {
        itemService.updateItem(itemForm.getId(), itemForm.getName(), itemForm.getPrice(),
                itemForm.getStockQuantity(), itemForm.getDetail(), itemForm.getGrade());
        return "redirect:/items/list";
    }
}
