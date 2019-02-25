package skeleton.web.security.remember;

import skeleton.entity.User;
import skeleton.web.security.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Beldon
 */
public interface RememberMeService {

    Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);

    void login(HttpServletRequest request, HttpServletResponse response, User user);

    void logout(HttpServletRequest request, HttpServletResponse response);

}
