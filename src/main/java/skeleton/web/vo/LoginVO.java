package skeleton.web.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author Beldon
 */
@Data
@ToString
public class LoginVO {
    @NotEmpty(message = "account can't be empty")
    private String account;

    @NotEmpty(message = "password can't be empty")
    private String password;

}
