package skeleton.web.controller;

import skeleton.entity.User;
import skeleton.web.security.Authentication;
import skeleton.web.security.AuthenticationContextHolder;

/**
 * @author Beldon
 */
public abstract class BaseController {
    protected User currentUser() {
        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        return authentication.user();
    }
}
