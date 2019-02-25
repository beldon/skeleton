package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Option;

/**
 * @author Beldon
 */
public interface OptionAutoRepo extends JpaRepository<Option, String> {
}
