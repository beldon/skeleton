package skeleton.web.controller.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import skeleton.AbstractControllerTest;
import skeleton.entity.User;
import skeleton.repo.UserAutoRepo;
import skeleton.service.PasswordService;
import skeleton.web.controller.MockUtils;
import skeleton.web.vo.LoginVO;

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

        mockMvc.perform(MockUtils.populatePostBuilder("/pub/user/login", loginVO))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));
    }
}