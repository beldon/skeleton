package skeleton.web.controller.api;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skeleton.entity.User;
import skeleton.web.controller.BaseController;
import skeleton.web.vo.ResponseVO;
import skeleton.web.vo.UserVO;

/**
 * @author Beldon
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseController {
    @GetMapping
    public ResponseVO<UserVO> getInfo() {
        UserVO userVO = new UserVO();
        User user = currentUser();
        BeanUtils.copyProperties(user, userVO);
        return ResponseVO.successWithData(userVO);
    }
}
