package shopping.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.mall.domain.Item;
import shopping.mall.domain.Member;
import shopping.mall.domain.Order;
import shopping.mall.domain.OrderDetail;
import shopping.mall.repository.ItemRepository;
import shopping.mall.repository.MemberRepository;
import shopping.mall.repository.OrderDetailRepository;
import shopping.mall.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderDetailRepository orderDetailRepository;

    // 주문상세 생성
    @Transactional
    public OrderDetail makeOrderDetail(Long itemId, int count) {
        Item item = itemRepository.findOne(itemId);
        OrderDetail orderDetail = OrderDetail.createOrderDetail(item, count);
        orderDetailRepository.save(orderDetail);
        return orderDetail;
    }

    // 주문
    @Transactional
    public Long order(Long memberId, List<OrderDetail> orderDetails) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        //주문 생성
        Order order = Order.createOrder(member, orderDetails);
        //주문 저장
        return orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId);
        //주문 취소
        order.cancel();
    }

    // 주문 전체조회
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }


}
