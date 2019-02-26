package skeleton.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author Beldon
 */
@Getter
@Setter
public class LoginVO {
    @NotEmpty(message = "account can't be empty")
    private String account;

    @NotEmpty(message = "password can't be empty")
    private String password;

}
