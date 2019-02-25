package skeleton.web.security.remember;

import org.springframework.web.filter.OncePerRequestFilter;
import skeleton.constant.CommonConstant;
import skeleton.web.security.Authentication;
import skeleton.web.security.AuthenticationContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Beldon
 */
public class RememberMeFilter extends OncePerRequestFilter {

    private final RememberMeService rememberMeServices;

    public RememberMeFilter(RememberMeService rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        if (authentication == null) {
            Authentication rememberMeAuth = rememberMeServices.autoLogin(request, response);
            if (rememberMeAuth != null) {
                AuthenticationContextHolder.setAuthentication(rememberMeAuth);
                request.getSession().setAttribute(CommonConstant.Session.AUTHENTICATION, rememberMeAuth);
                request.getSession().setAttribute(CommonConstant.Session.USER_ID, rememberMeAuth.user().getId());
            }
        }

        chain.doFilter(request, response);
    }
}
