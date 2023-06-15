package shopping.mall.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String mainAddress;
    private String subAddress;
    private String zipCode;

}
