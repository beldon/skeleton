package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Post;

/**
 * @author Beldon
 */
public interface PostAutoRepo extends JpaRepository<Post, String> {
    int countAllByCategoryId(String categoryId);
}
