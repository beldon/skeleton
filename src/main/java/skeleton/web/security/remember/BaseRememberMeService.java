package skeleton.web.security.remember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import skeleton.constant.CommonConstant;
import skeleton.entity.User;
import skeleton.enums.AuthType;
import skeleton.exception.CookieParseException;
import skeleton.repo.UserAutoRepo;
import skeleton.web.security.Authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * @author Beldon
 */
@Getter
@Slf4j
public abstract class BaseRememberMeService implements RememberMeService {
    private static final String DELIMITER = ":";
    private static final int COOKIE_LENGTH = 3;
    private String rememberKey = CommonConstant.DEFAULT_REMEMBER_ME_KEY;
    private int expiry = CommonConstant.DEFAULT_REMEMBER_ME_EXPIRY;

    private final UserAutoRepo userAutoRepo;

    public BaseRememberMeService(UserAutoRepo userAutoRepo) {

        this.userAutoRepo = userAutoRepo;
    }

    public BaseRememberMeService(String rememberKey, int expiry, UserAutoRepo userAutoRepo) {
        this.rememberKey = rememberKey;
        this.expiry = expiry;
        this.userAutoRepo = userAutoRepo;
    }


    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie rememberCookie = getRememberCookie(request);
        if (rememberCookie == null) {
            return null;
        }
        CookieTokens cookieTokens = decodeCookie(rememberCookie.getValue());
        if (isTokenExpired(cookieTokens.getExpiryTime())) {
            return null;
        }
        String account = cookieTokens.getAccount();
        Optional<User> userOptional = userAutoRepo.findByAccount(account);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        if (!isValidSignature(user, cookieTokens.signature)) {
            return null;
        }

        String signature = makeTokenSignature(calculateLoginLifetime(), user);
        setCookie(user, signature, response);
        autoLoginSuccess(request, response, user, cookieTokens.getSignature(), signature);
        return new Authentication(AuthType.REMEMBER_ME, user);
    }

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response, User user) {
        Authentication authentication = new Authentication(AuthType.AUTHENTICATED, user);
        request.getSession().setAttribute(CommonConstant.Session.AUTHENTICATION, authentication);
        String signature = makeTokenSignature(calculateLoginLifetime(), user);
        setCookie(user, signature, response);
        loginSuccess(request, response, user, signature);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie rememberCookie = getRememberCookie(request);
        if (rememberCookie == null) {
            return;
        }
        rememberCookie.setMaxAge(0);
        rememberCookie.setValue("");
        rememberCookie.setPath("/");
        response.addCookie(rememberCookie);
        CookieTokens cookieTokens = decodeCookie(rememberCookie.getValue());
        clearToken(cookieTokens.getSignature());
    }


    protected void clearToken(String token) {

    }

    protected void loginSuccess(HttpServletRequest request, HttpServletResponse response, User user, String signature) {

    }

    protected void autoLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                                    User user, String oldSignature, String newSignature) {

    }

    protected abstract String makeTokenSignature(long expiryTime, User user);

    protected abstract boolean isValidSignature(User user, String signature);


    protected long calculateLoginLifetime() {
        return System.currentTimeMillis() + expiry * 1000;
    }

    private void setCookie(User user, String signature, HttpServletResponse response) {
        long expiryTime = calculateLoginLifetime();
        String encodeCookie = encodeCookie(expiryTime, user.getAccount(), signature);
        Cookie cookie = new Cookie(rememberKey, encodeCookie);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(getExpiry());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String encodeCookie(long expiryTime, String account, String signature) {
        String value = expiryTime +
                DELIMITER +
                account +
                DELIMITER +
                signature;
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private CookieTokens decodeCookie(String cookieValue) {
        String cookies = new String(Base64.getDecoder().decode(cookieValue), StandardCharsets.UTF_8);
        String[] cookiesArr = cookies.split(DELIMITER);
        String[] tokens = StringUtils.delimitedListToStringArray(cookies, DELIMITER);
        if (cookiesArr.length != COOKIE_LENGTH) {
            String msg = String.format("[%s] illegal cookie, parse error", cookieValue);
            log.error(msg);
            throw new CookieParseException(msg);
        }
        return new CookieTokens(Long.parseLong(tokens[0]), tokens[1], tokens[2]);
    }

    private Cookie getRememberCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (rememberKey.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    protected boolean isTokenExpired(long tokenExpiryTime) {
        return tokenExpiryTime < System.currentTimeMillis();
    }

    @Data
    @AllArgsConstructor
    private static final class CookieTokens {
        private long expiryTime;

        private String account;

        private String signature;
    }
}

