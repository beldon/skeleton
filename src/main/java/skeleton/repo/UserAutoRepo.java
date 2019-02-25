package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.User;

import java.util.Optional;

/**
 * @author Beldon
 */
public interface UserAutoRepo extends JpaRepository<User, String> {
    Optional<User> findByAccount(String account);
}
