package shopping.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int count;
    private int orderPrice;

    //==생성 메서드==//
    public static OrderDetail createOrderDetail(Item item, int count) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setItem(item);
        orderDetail.setOrderPrice(item.getPrice() * count);
        orderDetail.setCount(count);
        return orderDetail;
    }

    //==비즈니스 로직==//
    /** 주문 취소 */
    public void cancel() {
        getItem().addStock(count);
    }


}
