package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Link;

/**
 * @author Beldon
 */
public interface LinkAutoRepo extends JpaRepository<Link, String> {
}
