package skeleton.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author Beldon
 */
@Getter
@Setter
public class CategoryModifyVO {
    @NotEmpty(message = "name can't be empty")
    private String name;
    private String url;
    private Integer sort;
    private String description;
    private String parentId;
}
