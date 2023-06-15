package shopping.mall.Form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import shopping.mall.domain.Category;

import java.time.LocalDate;

@Getter
@Setter
public class ItemForm {

    private Long id;

    @NotEmpty(message = "* 상품명을 입력해주세요")
    @Size(max = 20, message = "상품명은 20자까지만 작성 가능합니다.")
    private String name;
    @NotNull(message = "* 재고수량을 입력해주세요")
    @Digits(integer = 100, fraction = 0, message = "재고수량은 최대 100자리까지만 작성 가능합니다.")
    private int stockQuantity;
    @NotNull(message = "* 가격을 입력해주세요")
    @Digits(integer = 100, fraction = 0, message = "가격은 최대 100자리까지만 작성 가능합니다.")
    private int price;
    @Size(max=2000, message = "상품 상세란은 2000자까지만 작성 가능합니다.")
    private String detail;
    @DecimalMax(value = "5")
    private int grade;

}
