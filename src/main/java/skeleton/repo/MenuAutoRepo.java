package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Menu;

/**
 * @author Beldon
 */
public interface MenuAutoRepo extends JpaRepository<Menu, String> {
}
