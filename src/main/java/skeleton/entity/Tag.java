package skeleton.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Beldon
 */
@Entity
@Table(name = "tag")
@Data
public class Tag implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    @Column(unique = true)
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    private Integer count;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_post_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> posts;


}
