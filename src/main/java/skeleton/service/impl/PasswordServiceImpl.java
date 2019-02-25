package skeleton.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import skeleton.common.util.ShaUtils;
import skeleton.service.PasswordService;

/**
 * @author Beldon
 */
@Service
public class PasswordServiceImpl implements PasswordService {
    @Override
    public String encode(String password) {
        Assert.hasText(password, "password can not be empty");
        return ShaUtils.sha256(password);
    }

    @Override
    public boolean checkPassword(String raw, String encodePass) {
        return encode(raw).equals(encodePass);
    }
}
