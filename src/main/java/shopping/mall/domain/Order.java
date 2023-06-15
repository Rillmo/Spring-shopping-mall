package shopping.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDate orderDate;

    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private int totalPrice;

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, List<OrderDetail> orderDetails) {
        Order order = new Order();
        order.setMember(member);
        for (OrderDetail o : orderDetails) {
            order.addOrderDetail(o);
            order.setTotalPrice(order.getTotalPrice()+ o.getOrderPrice());
        }
        order.setStatus("ORDER");
        order.setOrderDate(LocalDate.now());
        return order;
    }
    //==비즈니스 로직==//
    /** 주문 취소 */
    public void cancel() {
        this.setStatus("CANCEL");
        for (OrderDetail o : orderDetails) {
            o.cancel();
        }
    }

}
