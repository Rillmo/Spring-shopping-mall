package shopping.mall.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shopping.mall.domain.*;
import shopping.mall.repository.CartDetailRepository;
import shopping.mall.service.CartService;
import shopping.mall.service.ItemService;
import shopping.mall.service.MemberService;
import shopping.mall.service.OrderService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final MemberService memberService;
    private final CartService cartService;
    private final ItemService itemService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderService orderService;

    // 징바구니 조회
    @GetMapping("/list")
    public String cartList(@Nullable @RequestParam("errMessage") String errMessage,
                           @Nullable @RequestParam("errItem") String errItem,
                           Principal principal, Model model) {
        log.info(principal.getName());  // userId
        // 현재 로그인한 member 조회
        Member member = memberService.findByUserId(principal.getName()).get(0);

        // member의 cart 조회
        Cart cart = cartService.findCartByMemberId(member.getId());

        // cart가 생성되지 않은 경우 생성
        if (cart == null) {
            cart = cartService.createCart(member);
        }
        //log.info(cart.getId().toString());

        // cart의 가격총합 계산
        List<CartDetail> cartDetails = cartService.memberCartView(cart);
        int totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getCount() * cartDetail.getItem().getPrice();
        }
        //log.info(String.valueOf(totalPrice));

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("memberName", member.getName());
        if (errMessage != "") {
            model.addAttribute("errMessage", errMessage);
            model.addAttribute("errItem", errItem);
        }
        return "carts/cartList";
    }

    // 상품 추가
    @GetMapping("/{id}/add")
    public String addItem(@PathVariable("id") Long itemId, Principal principal,
                          HttpServletRequest request, RedirectAttributes redirect) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        Item item = itemService.findItemById(itemId);
        log.info("itemId = " + itemId.toString());
        try {
            cartService.addCart(member, item, 1);
        } catch (IllegalStateException e) {
            redirect.addAttribute("errMessage", "재고부족");
            redirect.addAttribute("errItem", item.getName());
        }
        return "redirect:/cart/list";
    }

    // 상품 감소
    @GetMapping("/{id}/subtract")
    public String subtractItem(@PathVariable("id") Long itemId, Principal principal) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        Item item = itemService.findItemById(itemId);
        cartService.subtractCart(member, item, 1);
        return "redirect:/cart/list";
    }

    // 장바구니 상세 삭제
    @GetMapping("/{id}/delete")
    public String deleteCartDetail(@PathVariable("id") Long cartDetailId, Principal principal) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        Optional<CartDetail> cartDetail = cartDetailRepository.findById(cartDetailId);
        cartService.cartDeleteDetail(member, cartDetail.get());
        return "redirect:/cart/list";
    }

    // 장바구니 전체 삭제
    @GetMapping("/deleteAll")
    public String deleteAllCartDetail(Principal principal) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        Cart cart = cartService.findCartByMemberId(member.getId());
        cartService.cartDeleteAll(cart);
        return "redirect:/cart/list";
    }

}
