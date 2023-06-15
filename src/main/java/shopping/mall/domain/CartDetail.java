package shopping.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class CartDetail {
    @Id
    @GeneratedValue
    @Column(name = "cart_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDate createDate;
    // 상품 개수
    private int count;

    public void addCount(int count) {
        this.count += count;
    }
    public void subtractCount(int count) {
        this.count -= count;
    }

    public static CartDetail createCartDetail(Cart cart, Item item, int count) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setItem(item);
        cartDetail.setCount(count);
        cartDetail.setCreateDate(LocalDate.now());
        return cartDetail;
    }

    @Override
    public String toString() {
        return "CartDetail{" + "\n" +
                "id=" + this.id +"\n" +
                "cart=" + this.cart.getId() +"\n" +
                "item=" + this.item.getId() +"\n" +
                "count=" + this.count +"\n" +
                '}';
    }
}
