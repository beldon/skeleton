package skeleton.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import skeleton.enums.PostStatus;
import skeleton.enums.PostType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Beldon
 */
@Entity
@Table(name = "post")
@Data
public class Post implements Serializable {

    public static final Integer STATUS_RELEASED = 0;
    public static final Integer STATUS_DRAFT = 1;
    public static final Integer STATUS_RECYCLE = 2;

    public static final String TYPE_POST = "post";
    public static final String TYPE_PAGE = "page";

    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name = "customerId", strategy = "skeleton.common.IdGenerator")
    private String id;

    private String title;

    private String summary;

    @Column(name = "content_html")
    @Lob
    private String contentHtml;

    @Column(name = "content_md")
    @Lob
    private String contentMd;

    @Column(length = 10)
    private String uid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    private User user;

    @Column(unique = true)
    private String url;

    private PostType type;

    private PostStatus status;

    @Column(name = "category_id", length = 10)
    private String categoryId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    private Integer views;

    @Column(name = "release_time")
    private Date releaseTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;


}
