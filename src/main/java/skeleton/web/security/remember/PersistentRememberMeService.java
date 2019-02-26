package skeleton.web.security.remember;

import skeleton.common.util.IpUtils;
import skeleton.entity.RememberMeToken;
import skeleton.entity.User;
import skeleton.repo.RememberMeTokenAutoRepo;
import skeleton.repo.UserAutoRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Beldon
 */
public class PersistentRememberMeService extends BaseRememberMeService {

    private int maxTokenNum = 2;
    private final RememberMeTokenAutoRepo rememberMeTokenAutoRepo;

    public PersistentRememberMeService(RememberMeTokenAutoRepo rememberMeTokenAutoRepo, UserAutoRepo userAutoRepo) {
        super(userAutoRepo);
        this.rememberMeTokenAutoRepo = rememberMeTokenAutoRepo;
    }

    public PersistentRememberMeService(RememberMeTokenAutoRepo rememberMeTokenAutoRepo,
                                       UserAutoRepo userAutoRepo, int maxTokenNum) {
        super(userAutoRepo);
        this.rememberMeTokenAutoRepo = rememberMeTokenAutoRepo;
        this.maxTokenNum = maxTokenNum;
    }

    public PersistentRememberMeService(RememberMeTokenAutoRepo rememberMeTokenAutoRepo,
                                       UserAutoRepo userAutoRepo, int maxTokenNum, String rememberKey, int expiry) {
        super(userAutoRepo, rememberKey, expiry);
        this.rememberMeTokenAutoRepo = rememberMeTokenAutoRepo;
        this.maxTokenNum = maxTokenNum;
    }

    @Override
    protected void loginSuccess(HttpServletRequest request, HttpServletResponse response, User user, String signature) {
        checkAndCleanMoreToken(user.getAccount());
        saveNewToken(user.getAccount(), signature, request);
    }

    @Override
    protected void autoLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                                    User user, String oldSignature, String newSignature) {
        Optional<RememberMeToken> rememberMeTokenOptional = rememberMeTokenAutoRepo.findByAccountAndToken(
                user.getAccount(), oldSignature);
        if (rememberMeTokenOptional.isEmpty()) {
            checkAndCleanMoreToken(user.getAccount());
            saveNewToken(user.getAccount(), newSignature, request);
            return;
        }

        RememberMeToken rememberMeToken = rememberMeTokenOptional.get();
        rememberMeToken.setUpdateTime(new Date());
        rememberMeToken.setToken(newSignature);
        rememberMeTokenAutoRepo.save(rememberMeToken);
    }

    @Override
    protected String makeTokenSignature(long expiryTime, User user) {
        return UUID.randomUUID().toString();
    }

    @Override
    protected boolean isValidSignature(User user, String signature) {
        Optional<RememberMeToken> rememberMeTokenOptional = rememberMeTokenAutoRepo.findByAccountAndToken(
                user.getAccount(), signature);
        return rememberMeTokenOptional.isPresent();
    }

    @Override
    protected void clearToken(String account, String token) {
        rememberMeTokenAutoRepo.findByAccountAndToken(account, token)
                .ifPresent(rememberMeTokenAutoRepo::delete);
    }

    private void checkAndCleanMoreToken(String account) {
        List<RememberMeToken> tokens = rememberMeTokenAutoRepo.findByAccountOrderByCreateTime(account);
        if (tokens.size() >= maxTokenNum) {
            int end = tokens.size() - maxTokenNum + 1;
            for (int i = 0; i < end; i++) {
                rememberMeTokenAutoRepo.delete(tokens.get(i));
            }
        }
    }

    private void saveNewToken(String account, String token, HttpServletRequest request) {
        RememberMeToken rememberMeToken = new RememberMeToken();
        rememberMeToken.setCreateTime(new Date());
        rememberMeToken.setUpdateTime(new Date());
        rememberMeToken.setAccount(account);
        rememberMeToken.setToken(token);
        rememberMeToken.setIp(IpUtils.getRequestRealIp(request));
        rememberMeToken.setUserAgent(getUserAgent(request));
        rememberMeTokenAutoRepo.save(rememberMeToken);
    }

    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }
}
