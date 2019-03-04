package skeleton.web.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Beldon
 */
@Getter
@Setter
public class UserVO {
    private String id;
    private String account;
    private String nickname;
    private String avatar;
    private Date lastLoginTime;
}
