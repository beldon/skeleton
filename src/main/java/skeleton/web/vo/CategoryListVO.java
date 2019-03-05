package skeleton.web.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Beldon
 */
@Getter
@Setter
public class CategoryListVO {
    private String id;
    private String name;
    private String url;
    private Integer sort;
    private String description;
    private String parentId;
    private List<CategoryListVO> children;
}
