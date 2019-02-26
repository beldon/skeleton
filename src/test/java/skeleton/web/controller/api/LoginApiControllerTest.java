package skeleton.web.controller.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import skeleton.AbstractControllerTest;
import skeleton.constant.CommonConstant;
import skeleton.entity.User;
import skeleton.repo.UserAutoRepo;
import skeleton.service.PasswordService;
import skeleton.web.controller.MockUtils;
import skeleton.web.security.Authentication;
import skeleton.web.vo.LoginVO;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Beldon
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginApiControllerTest extends AbstractControllerTest {

    @Autowired
    private UserAutoRepo userAutoRepo;

    @Autowired
    private PasswordService passwordService;

    private String rawPassword = UUID.randomUUID().toString();

    private User user;

    @Before
    public void initUser() {
        userAutoRepo.deleteAll();
        user = new User();
        user.setAccount(UUID.randomUUID().toString());
        user.setPassword(passwordService.encode(rawPassword));
        user.setNickname(user.getAccount());
        userAutoRepo.save(user);
    }

    @Test
    public void login() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setAccount(user.getAccount());
        loginVO.setPassword(rawPassword);

        ResultActions resultActions = mockMvc.perform(MockUtils.populatePostBuilder("/pub/user/login", loginVO))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));

        HttpSession session = resultActions.andReturn().getRequest().getSession();
        Authentication authentication = (Authentication) session.getAttribute(CommonConstant.Session.AUTHENTICATION);
        Assert.assertNotNull(authentication);
        Assert.assertTrue(authentication.isAuthenticated());
        User user = authentication.user();

        Assert.assertEquals(this.user.getId(), user.getId());
        Assert.assertEquals(this.user.getAccount(), user.getAccount());


        loginVO = new LoginVO();
        loginVO.setAccount(user.getAccount());
        loginVO.setPassword(UUID.randomUUID().toString());
        mockMvc.perform(MockUtils.populatePostBuilder("/pub/user/login", loginVO))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("1"));

        loginVO = new LoginVO();
        loginVO.setAccount(UUID.randomUUID().toString());
        loginVO.setPassword(UUID.randomUUID().toString());
        mockMvc.perform(MockUtils.populatePostBuilder("/pub/user/login", loginVO))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("1"));

    }
}