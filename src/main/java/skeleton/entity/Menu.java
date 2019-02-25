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
@Table(name = "menu")
@Data
public class Menu implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    private String name;

    private String url;

    private Integer sort;

    private String target;

    private Date createTime;

    private Date updateTime;
}
