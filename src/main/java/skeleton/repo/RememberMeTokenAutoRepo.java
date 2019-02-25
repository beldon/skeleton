package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.RememberMeToken;

import java.util.List;
import java.util.Optional;

/**
 * @author Beldon
 */
public interface RememberMeTokenAutoRepo extends JpaRepository<RememberMeToken, String> {
    List<RememberMeToken> findByAccountOrderByCreateTime(String account);

    Optional<RememberMeToken> findByAccountAndToken(String account, String token);
}
