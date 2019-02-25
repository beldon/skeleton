package skeleton.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Beldon
 */
@Entity
@Table(name = "attachment")
@Data
public class Attachment implements Serializable {

    public static final String TYPE_IMAGE = "image";

    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "ext_name")
    private String extName;

    private String path;

    private String url;

    private String md5;

    private Long size;

    private String type;

    /**
     * eg：image/png  image/jpg
     */
    @Column(name = "context_type")
    private String contentType;

}
