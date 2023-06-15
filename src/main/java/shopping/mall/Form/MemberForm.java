package shopping.mall.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm{

    private Long id;

    @NotEmpty(message = "* 이름을 입력해주세요")
    private String name;
    @Size(min = 3, max = 20)
    @NotEmpty(message = "* 아이디를 입력해주세요")
    private String userId;
    @Size(min = 3, max = 20)
    @NotEmpty(message = "* 비밀번호를 입력해주세요")
    private String password;
    @Size(min = 3, max = 20)
    @NotEmpty(message = "* 비밀번호를 다시 입력해주세요")
    private String passwordCheck;

    //@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    //@Nullable
    private String phoneNum;
    @Email
    private String email;
    private String grade;

}
