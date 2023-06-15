package shopping.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "item")
    private List<CartDetail> cartDetailList;

    private String name;
    private int stockQuantity;
    private int price;
    private String detail;
    private LocalDate registDate;
    private LocalDate finalUpdateDate;
    private int views;
    private int grade;

    // 재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 재고 감소
    public void removeStock(int quantity) {
        if(this.stockQuantity-quantity < 0){
            throw new IllegalStateException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }
}
