package skeleton.web.controller.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import skeleton.entity.Category;
import skeleton.repo.CategoryAutoRepo;
import skeleton.repo.PostAutoRepo;
import skeleton.web.controller.BaseController;
import skeleton.web.vo.CategoryListVO;
import skeleton.web.vo.CategoryModifyVO;
import skeleton.web.vo.CategoryVO;
import skeleton.web.vo.ResponseVO;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Beldon
 */
@RestController
@RequestMapping("/api/category")
public class CategoryApiController extends BaseController {

    private final CategoryAutoRepo categoryAutoRepo;
    private final PostAutoRepo postAutoRepo;

    @Autowired
    public CategoryApiController(CategoryAutoRepo categoryAutoRepo, PostAutoRepo postAutoRepo) {
        this.categoryAutoRepo = categoryAutoRepo;
        this.postAutoRepo = postAutoRepo;
    }

    @PostMapping
    public ResponseVO addCategory(@RequestBody @Valid CategoryModifyVO categoryModifyVO) {
        String url = categoryModifyVO.getUrl();
        if (StringUtils.hasText(url)) {
            Optional<Category> categoryOptional = categoryAutoRepo.findByUrl(url);
            if (categoryOptional.isPresent()) {
                return ResponseVO.error("url exists");
            }
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryModifyVO, category);
        category.setCreateTime(new Date());
        String parentId = getRealParentId(categoryModifyVO.getParentId());
        category.setUrl(StringUtils.hasText(category.getUrl()) ? category.getUrl() : null);
        category.setParentId(parentId);
        categoryAutoRepo.save(category);
        return ResponseVO.success();
    }

    @PutMapping("/{id}")
    public ResponseVO updateCategory(@PathVariable String id, @RequestBody @Valid CategoryModifyVO categoryModifyVO) {
        Optional<Category> categoryOptional = categoryAutoRepo.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseVO.error("record not exists.");
        }
        Category category = categoryOptional.get();
        String url = categoryModifyVO.getUrl();
        if (StringUtils.hasText(url)) {
            Optional<Category> urlCateOptional = categoryAutoRepo.findByUrl(url);
            if (urlCateOptional.isPresent() && !urlCateOptional.get().getId().equals(category.getId())) {
                return ResponseVO.error("url exists.");
            }
        }
        String parentId = getRealParentId(categoryModifyVO.getParentId());
        category.setName(categoryModifyVO.getName());
        category.setUrl(StringUtils.hasText(categoryModifyVO.getUrl()) ? categoryModifyVO.getUrl() : null);
        category.setSort(categoryModifyVO.getSort());
        category.setDescription(categoryModifyVO.getDescription());
        category.setParentId(parentId);
        categoryAutoRepo.save(category);
        return ResponseVO.success();
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteCategory(@PathVariable String id) {
        Optional<Category> categoryOptional = categoryAutoRepo.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseVO.error("record not exists");
        }

        Category category = categoryOptional.get();
        int subCategoryCount = categoryAutoRepo.countAllByParentId(category.getId());
        if (subCategoryCount > 0) {
            return ResponseVO.error("stop delete. has sub category");
        }

        int count = postAutoRepo.countAllByCategoryId(category.getId());
        if (count > 0) {
            return ResponseVO.error("stop delete. there are articles in this category");
        }
        categoryAutoRepo.delete(categoryOptional.get());
        return ResponseVO.success();
    }

    @GetMapping("/{id}")
    public ResponseVO<CategoryVO> getCategory(@PathVariable String id) {
        Optional<Category> categoryOptional = categoryAutoRepo.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseVO.error("record not exists");
        }
        Category category = categoryOptional.get();
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        return ResponseVO.successWithData(categoryVO);
    }


    @GetMapping
    public ResponseVO<List<CategoryListVO>> listCategory() {
        List<CategoryListVO> categoryListVOS = findByParent(Category.ROOT);
        return ResponseVO.successWithData(categoryListVOS);
    }


    private List<CategoryListVO> findByParent(String parentId) {
        List<CategoryListVO> categoryListVOS = new ArrayList<>();
        if (StringUtils.hasText(parentId)) {
            List<Category> categories = categoryAutoRepo.findByParentId(parentId);
            for (Category category : categories) {
                CategoryListVO categoryListVO = new CategoryListVO();
                BeanUtils.copyProperties(category, categoryListVO);
                categoryListVO.setChildren(findByParent(category.getId()));
                categoryListVOS.add(categoryListVO);
            }
        }
        return categoryListVOS;
    }

    private String getRealParentId(String parentId) {
        if (StringUtils.hasText(parentId)) {
            Optional<Category> parentCategory = categoryAutoRepo.findById(parentId);
            if (parentCategory.isEmpty()) {
                return Category.ROOT;
            }
        } else {
            return Category.ROOT;
        }
        return parentId;
    }
}
