package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * @author Beldon
 */
public interface CategoryAutoRepo extends JpaRepository<Category, String> {
    Optional<Category> findByUrl(String url);

    List<Category> findByParentId(String parentId);
}
