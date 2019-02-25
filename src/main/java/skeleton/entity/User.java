package skeleton.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author BeldonZL
 */
@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;
    private String account;
    private String nickname;
    private String password;
    private String avatar;

    @Column(name = "last_login_time")
    private Date lastLoginTime;
}
