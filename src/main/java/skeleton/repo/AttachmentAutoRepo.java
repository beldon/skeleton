package skeleton.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skeleton.entity.Attachment;

/**
 * @author Beldon
 */
public interface AttachmentAutoRepo extends JpaRepository<Attachment, String> {
}
