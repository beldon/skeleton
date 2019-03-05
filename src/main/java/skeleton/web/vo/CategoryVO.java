package skeleton.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Beldon
 */
@Getter
@Setter
public class CategoryVO {
    private String id;
    private String name;
    private String url;
    private Integer sort;
    private String description;
    private String parentId;
}
