package shopping.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class  Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartDetails = new ArrayList<>();

    private LocalDate createDate;
    private int count;  // 카트에 담긴 상품 개수

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setCount(0);
        cart.setCreateDate(LocalDate.now());
        return cart;
    }
}
