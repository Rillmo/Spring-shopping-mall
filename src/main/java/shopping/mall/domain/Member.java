package shopping.mall.domain;

import jakarta.annotation.Nullable;
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
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String phoneNum;
    private String email;
    private String userId;
    private String password;
    private LocalDate joinDate;
    private LocalDate finalUpdateDate;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy ="member",fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
