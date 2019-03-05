package skeleton.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Beldon
 */
@Entity
@Table(name = "category")
@Data
public class Category implements Serializable {

    public static final String ROOT = "root";


    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;


    private String name;

    @Column(unique = true)
    private String url;

    private String description;

    /**
     * the bigger the higher the front
     */
    private Integer sort;

    @Column(name = "parent_id", length = 10)
    private String parentId;

    /**
     * default parent's id is root
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category parent;

    @Column(name = "create_time")
    private Date createTime;
}
