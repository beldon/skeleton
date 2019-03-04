package skeleton.web;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import skeleton.AbstractControllerTest;
import skeleton.entity.User;
import skeleton.repo.UserAutoRepo;
import skeleton.service.PasswordService;
import skeleton.web.controller.MockUtils;
import skeleton.web.vo.LoginVO;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Beldon
 */
public abstract class AbstractUserControllerTest extends AbstractControllerTest {
    @Autowired
    protected UserAutoRepo userAutoRepo;
    @Autowired
    protected PasswordService passwordService;

    protected String rawPassword = UUID.randomUUID().toString();
    protected User user;
    protected Cookie[] cookies;

    @Before
    public void before() throws Exception {
        userAutoRepo.deleteAll();
        user = new User();
        user.setAccount(UUID.randomUUID().toString());
        user.setPassword(passwordService.encode(rawPassword));
        user.setNickname(user.getAccount());
        userAutoRepo.save(user);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccount(user.getAccount());
        loginVO.setPassword(rawPassword);
        ResultActions resultActions = mockMvc.perform(MockUtils.populatePostBuilder("/pub/user/login", loginVO))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        this.cookies = response.getCookies();
        user = userAutoRepo.findById(user.getId()).get();
    }

}
