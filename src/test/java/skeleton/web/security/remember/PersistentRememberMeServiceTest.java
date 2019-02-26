package skeleton.web.security.remember;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import skeleton.AbstractTest;
import skeleton.constant.CommonConstant;
import skeleton.entity.RememberMeToken;
import skeleton.entity.User;
import skeleton.repo.RememberMeTokenAutoRepo;
import skeleton.repo.UserAutoRepo;
import skeleton.service.PasswordService;
import skeleton.web.security.Authentication;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Beldon
 * @create 2019-02-26 13:35
 */
public class PersistentRememberMeServiceTest extends AbstractTest {


    @Autowired
    private UserAutoRepo userAutoRepo;
    @Autowired
    private RememberMeTokenAutoRepo rememberMeTokenAutoRepo;
    @Autowired
    private PasswordService passwordService;

    private RememberMeService rememberMeService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private String rawPassword = UUID.randomUUID().toString();
    private User user;


    @Before
    public void before() {
        rememberMeTokenAutoRepo.deleteAll();

        rememberMeService = new PersistentRememberMeService(rememberMeTokenAutoRepo, userAutoRepo, 2);

        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();


        userAutoRepo.deleteAll();
        user = new User();
        user.setAccount(UUID.randomUUID().toString());
        user.setPassword(passwordService.encode(rawPassword));
        user.setNickname(user.getAccount());
        userAutoRepo.save(user);

    }

    @Test
    public void autoLogin() {
        List<RememberMeToken> tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(0, tokens.size());

        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);

        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        RememberMeToken firstToken = tokens.get(0);
        assertEquals(1, tokens.size());


        response = new MockHttpServletResponse();
        request.setCookies(rememberCookie);
        Authentication authentication = rememberMeService.autoLogin(request, response);
        assertNotNull(authentication);
        assertTrue(authentication.isRememberMe());

        User autoLoginUser = authentication.user();
        assertEquals(user.getId(), autoLoginUser.getId());
        assertEquals(user.getAccount(), autoLoginUser.getAccount());
        assertEquals(user.getNickname(), autoLoginUser.getNickname());

        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        RememberMeToken autoLoginToken = tokens.get(0);
        assertEquals(1, tokens.size());

        assertEquals(autoLoginToken.getId(), firstToken.getId());
        assertNotEquals(autoLoginToken.getToken(), firstToken.getToken());
    }

    @Test
    public void autoLogin_noCookie() {
        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);

        request = new MockHttpServletRequest();
        Cookie[] cookies = new Cookie[]{};
        request.setCookies(cookies);
        response = new MockHttpServletResponse();
        Authentication authentication = rememberMeService.autoLogin(request, response);
        assertNull(authentication);
    }

    @Test
    public void autoLogin_expire() throws Exception {
        rememberMeService = new PersistentRememberMeService(rememberMeTokenAutoRepo, userAutoRepo,
                2, CommonConstant.DEFAULT_REMEMBER_ME_KEY, 1);

        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);
        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        Authentication authentication = rememberMeService.autoLogin(request, response);
        assertNotNull(authentication);

        TimeUnit.SECONDS.sleep(1);

        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        authentication = rememberMeService.autoLogin(request, response);
        assertNull(authentication);
    }

    @Test
    public void autoLogin_noUser() {
        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);
        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        Authentication authentication = rememberMeService.autoLogin(request, response);
        assertNotNull(authentication);

        userAutoRepo.deleteAll();

        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        authentication = rememberMeService.autoLogin(request, response);
        assertNull(authentication);
    }

    @Test
    public void autoLogin_noValidToken() {
        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);
        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        Authentication authentication = rememberMeService.autoLogin(request, response);
        assertNotNull(authentication);

        List<RememberMeToken> tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(tokens.size(), 1);
        RememberMeToken rememberMeToken = tokens.get(0);
        rememberMeToken.setToken(UUID.randomUUID().toString());
        rememberMeTokenAutoRepo.save(rememberMeToken);

        request.setCookies(rememberCookie);
        response = new MockHttpServletResponse();
        authentication = rememberMeService.autoLogin(request, response);
        assertNull(authentication);
    }

    @Test
    public void login() {
        List<RememberMeToken> tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(0, tokens.size());

        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);

        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        RememberMeToken firstToken = tokens.get(0);
        assertEquals(1, tokens.size());

        rememberMeService.login(request, response, user);
        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(2, tokens.size());

        rememberMeService.login(request, response, user);
        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(2, tokens.size());


        for (RememberMeToken token : tokens) {
            assertNotEquals(token.getId(), firstToken.getId());
        }
    }

    @Test
    public void logout() {
        List<RememberMeToken> tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(0, tokens.size());

        rememberMeService.login(request, response, user);
        Cookie rememberCookie = response.getCookie(CommonConstant.DEFAULT_REMEMBER_ME_KEY);
        assertNotNull(rememberCookie);

        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        RememberMeToken firstToken = tokens.get(0);
        assertEquals(1, tokens.size());

        request.setCookies(rememberCookie);
        rememberMeService.logout(request, response);
        tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(user.getAccount());
        assertEquals(0, tokens.size());
    }
}