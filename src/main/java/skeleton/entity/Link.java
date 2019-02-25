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
@Table(name = "link")
@Data
public class Link implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    private String title;

    private String url;

    private String pic;

    private Integer sort;

    private String target;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
