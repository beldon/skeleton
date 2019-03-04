package skeleton.web.controller.api;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import skeleton.web.AbstractUserControllerTest;
import skeleton.web.controller.MockUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Beldon
 */
public class UserApiControllerTest extends AbstractUserControllerTest {

    @Test
    public void getInfo() throws Exception {
        mockMvc.perform(MockUtils.populateGetBuilder("/api/user").cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.account").value(user.getAccount()))
                .andExpect(jsonPath("$.data.nickname").value(user.getNickname()))
                .andExpect(jsonPath("$.data.avatar").value(user.getAvatar()))
                .andExpect(jsonPath("$.data.lastLoginTime").value(user.getLastLoginTime().getTime()));
    }
}