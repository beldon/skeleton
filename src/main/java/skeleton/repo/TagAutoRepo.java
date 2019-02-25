package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Tag;

import java.util.Optional;

/**
 * @author Beldon
 */
public interface TagAutoRepo extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String name);
}
