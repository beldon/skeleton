package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.RememberMeToken;

/**
 * @author Beldon
 */
public interface RememberMeTokenAutoRepo extends JpaRepository<RememberMeToken, String> {
}
