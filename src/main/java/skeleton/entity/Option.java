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
@Table(name = "option")
@Data
public class Option implements Serializable {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    @Column(name = "opt_group", length = 100)
    private String group;

    /**
     * key
     */
    @Column(name = "opt_key", length = 100)
    private String key;

    /**
     * value for value
     */
    @Column(name = "opt_value", columnDefinition = "text")
    private String value;

    /**
     * the record's create date
     */
    private Date createTime;

    /**
     * the record's update date
     */
    private Date updateTime;
}
