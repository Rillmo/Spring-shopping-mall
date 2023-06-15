package shopping.mall.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shopping.mall.domain.Item;
import shopping.mall.domain.Member;
import shopping.mall.service.ItemService;
import shopping.mall.service.MemberService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @RequestMapping("/")
    public String home(Model model) {

        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
