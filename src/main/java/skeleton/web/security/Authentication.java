package skeleton.web.security;


import skeleton.entity.User;
import skeleton.enums.AuthType;

import java.io.Serializable;

/**
 * @author Beldon
 */
public class Authentication implements Serializable {

    private final AuthType authType;
    private final User user;

    public Authentication(AuthType authType, User user) {
        this.user = user;
        this.authType = authType;
    }

    public boolean isRememberMe() {
        return authType.isRememberMe();
    }

    public boolean isAuthenticated() {
        return authType.isAuthenticated();
    }

    public boolean isGuest() {
        return authType.isGuest();
    }

    public User user() {
        return user;
    }
}
