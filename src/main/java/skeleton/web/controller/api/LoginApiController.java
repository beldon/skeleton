package skeleton.web.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skeleton.entity.User;
import skeleton.repo.UserAutoRepo;
import skeleton.service.PasswordService;
import skeleton.web.vo.LoginVO;
import skeleton.web.vo.ResponseVO;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Beldon
 */
@RestController
@RequestMapping("/pub/user")
@Slf4j
public class LoginApiController {

    private final UserAutoRepo userAutoRepo;
    private final PasswordService passwordService;

    public LoginApiController(UserAutoRepo userAutoRepo, PasswordService passwordService) {
        this.userAutoRepo = userAutoRepo;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseVO login(@Valid @RequestBody LoginVO loginVO) {
        Optional<User> userOptional = userAutoRepo.findByAccount(loginVO.getAccount());
        if (userOptional.isEmpty()) {
            log.warn("user [{}] does not exist.", loginVO.getAccount());
            return ResponseVO.error("user does not exist");
        }
        User user = userOptional.get();
        boolean checkResult = passwordService.checkPassword(loginVO.getPassword(), user.getPassword());
        if (!checkResult) {
            log.warn("[{}] pass error", loginVO.getAccount());
            return ResponseVO.error("password error");
        }
        return ResponseVO.success();
    }
}
