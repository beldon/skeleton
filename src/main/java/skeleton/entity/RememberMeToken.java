package skeleton.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Beldon
 */
@Entity
@Table(name = "remember_me_token")
@Data
public class RememberMeToken {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;
    private String account;
    private String token;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    private String ip;
    @Column(name = "user_agent")
    private String userAgent;
}
