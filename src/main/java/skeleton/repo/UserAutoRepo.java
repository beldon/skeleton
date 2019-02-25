package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.User;

/**
 * @author Beldon
 */
public interface UserAutoRepo extends JpaRepository<User, String> {
}
