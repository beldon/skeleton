package skeleton.web.security.remember;

import skeleton.entity.User;
import skeleton.web.security.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Beldon
 */
public interface RememberMeService {
    /**
     * default cookie key for remember me
     */
    String DEFAULT_REMEMBER_ME_KEY = "remember-me";

    /**
     * default remember me expiration time. default fortnight
     */
    int DEFAULT_REMEMBER_ME_EXPIRY = 60 * 60 * 24 * 14;


    Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);


    void login(HttpServletRequest request, HttpServletResponse response, User user);

    void logout(HttpServletRequest request, HttpServletResponse response);

}
