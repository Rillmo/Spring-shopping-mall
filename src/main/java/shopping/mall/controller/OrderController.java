package shopping.mall.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shopping.mall.domain.*;
import shopping.mall.service.CartService;
import shopping.mall.service.MemberService;
import shopping.mall.service.OrderService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final CartService cartService;


    // 주문
    @GetMapping("/makeOrder")
    public String makeOrder(Principal principal, Model model) {
        Member member = memberService.findByUserId(principal.getName()).get(0);
        Cart cart = cartService.findCartByMemberId(member.getId());

        List<CartDetail> cartDetails = cartService.memberCartView(cart);
        List<OrderDetail> orderDetails = new ArrayList<>();

        // 장바구니 빈 경우
        if (cartDetails.size() == 0) {
            model.addAttribute("errMessage", "장바구니내용없음");
            return "redirect:/cart/list";
        }

        // 장바구니 상세 목록을 전부 주문 상세로 등록
        for (CartDetail cartDetail : cartDetails) {
            orderDetails.add(orderService.makeOrderDetail(cartDetail.getItem().getId(), cartDetail.getCount()));
        }
        // 등록된 주문 상세들로 주문 생성
        orderService.order(member.getId(), orderDetails);
        // 장바구니 리셋
        cartService.cartReset(cart);

        return "redirect:/order/list";
    }

    // 주문 내역
    @GetMapping("/list")
    public String orderList(Principal principal, Model model) {
        List<Order> orders = orderService.findAllOrders();
        Member member = memberService.findByUserId(principal.getName()).get(0);
        List<Order> memberOrders = new ArrayList<>();

        // 현재 로그인한 회원의 주문 리스트
        for (Order order : orders) {
            if (order.getMember().getId() == member.getId()) {
                memberOrders.add(order);
            }
        }

        model.addAttribute("orders", memberOrders);
        return "orders/orderList";
    }

    // 주문 취소(사용자)
    @GetMapping("/{id}/cancel/user")
    public String cancelOrderUser(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);

        return "redirect:/order/list ";
    }

    // 주문 취소(관리자)
    @GetMapping("/{id}/cancel/admin")
    public String cancelOrderAdmin(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);

        return "redirect:/order/list/admin ";
    }

    // 관리자용 주문 목록
    @GetMapping("/list/admin")
    public String orderListAdmin(Model model) {
        List<Order> orders = orderService.findAllOrders();

        model.addAttribute("orders", orders);
        return "orders/orderListAdmin";
    }

}
