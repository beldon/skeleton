package skeleton.web.controller.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import skeleton.entity.Category;
import skeleton.repo.CategoryAutoRepo;
import skeleton.web.AbstractUserControllerTest;
import skeleton.web.controller.MockUtils;
import skeleton.web.vo.CategoryModifyVO;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Beldon
 */
public class CategoryApiControllerTest extends AbstractUserControllerTest {

    @Autowired
    private CategoryAutoRepo categoryAutoRepo;

    @Before
    public void before() {
        categoryAutoRepo.deleteAll();
    }

    @Test
    public void addCategory_parameter_error() throws Exception {
        CategoryModifyVO categoryModifyVO = new CategoryModifyVO();

        mockMvc.perform(MockUtils.populatePostBuilder("/api/category", categoryModifyVO).cookie(cookies))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void addCategory_success() throws Exception {
        CategoryModifyVO categoryModifyVO = new CategoryModifyVO();
        categoryModifyVO.setName(UUID.randomUUID().toString());
        categoryModifyVO.setUrl(UUID.randomUUID().toString());
        categoryModifyVO.setSort(Math.abs(new Random().nextInt()));
        categoryModifyVO.setDescription(UUID.randomUUID().toString());

        mockMvc.perform(MockUtils.populatePostBuilder("/api/category", categoryModifyVO).cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));

        List<Category> all = categoryAutoRepo.findAll();
        Assert.assertEquals(1, all.size());
        Category category = all.get(0);
        Assert.assertEquals(category.getName(), categoryModifyVO.getName());
        Assert.assertEquals(category.getUrl(), categoryModifyVO.getUrl());
        Assert.assertEquals(category.getSort(), categoryModifyVO.getSort());
        Assert.assertEquals(category.getDescription(), categoryModifyVO.getDescription());
    }

    @Test
    public void addCategory_url_repeat() throws Exception {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setUrl(UUID.randomUUID().toString());
        category.setSort(Math.abs(new Random().nextInt()));
        category.setDescription(UUID.randomUUID().toString());
        categoryAutoRepo.save(category);

        CategoryModifyVO newCategoryModifyVO = new CategoryModifyVO();
        newCategoryModifyVO.setName(UUID.randomUUID().toString());
        newCategoryModifyVO.setUrl(category.getUrl());
        newCategoryModifyVO.setSort(Math.abs(new Random().nextInt()));
        newCategoryModifyVO.setDescription(UUID.randomUUID().toString());

        mockMvc.perform(MockUtils.populatePostBuilder("/api/category", newCategoryModifyVO).cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("1"));
    }

    @Test
    public void updateCategory() throws Exception {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setUrl(UUID.randomUUID().toString());
        category.setSort(Math.abs(new Random().nextInt()));
        category.setDescription(UUID.randomUUID().toString());
        categoryAutoRepo.save(category);

        CategoryModifyVO newCategoryModifyVO = new CategoryModifyVO();
        newCategoryModifyVO.setName(UUID.randomUUID().toString());
        newCategoryModifyVO.setUrl(category.getUrl());
        newCategoryModifyVO.setSort(Math.abs(new Random().nextInt()));
        newCategoryModifyVO.setDescription(UUID.randomUUID().toString());

        mockMvc.perform(MockUtils.populatePutBuilder("/api/category/" + category.getId(), newCategoryModifyVO).cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));

        List<Category> all = categoryAutoRepo.findAll();
        Assert.assertEquals(1, all.size());
        category = all.get(0);
        Assert.assertEquals(category.getName(), newCategoryModifyVO.getName());
        Assert.assertEquals(category.getUrl(), newCategoryModifyVO.getUrl());
        Assert.assertEquals(category.getSort(), newCategoryModifyVO.getSort());
        Assert.assertEquals(category.getDescription(), newCategoryModifyVO.getDescription());
    }

    @Test
    public void deleteCategory() throws Exception {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setUrl(UUID.randomUUID().toString());
        category.setSort(Math.abs(new Random().nextInt()));
        category.setDescription(UUID.randomUUID().toString());
        categoryAutoRepo.save(category);

        mockMvc.perform(MockUtils.populateDeleteBuilder("/api/category/" + category.getId(), null).cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("0"));

        List<Category> all = categoryAutoRepo.findAll();
        Assert.assertEquals(0, all.size());
    }

    @Test
    public void getCategory() throws Exception {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setUrl(UUID.randomUUID().toString());
        category.setSort(Math.abs(new Random().nextInt()));
        category.setDescription(UUID.randomUUID().toString());
        categoryAutoRepo.save(category);

        mockMvc.perform(MockUtils.populateGetBuilder("/api/category/" + category.getId()).cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.name").value(category.getName()))
                .andExpect(jsonPath("$.data.url").value(category.getUrl()))
                .andExpect(jsonPath("$.data.sort").value(category.getSort()))
                .andExpect(jsonPath("$.data.description").value(category.getDescription()))
                .andExpect(jsonPath("$.code").value("0"));
    }

    @Test
    public void listCategory() throws Exception {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setUrl(UUID.randomUUID().toString());
        category.setSort(Math.abs(new Random().nextInt()));
        category.setDescription(UUID.randomUUID().toString());
        category.setParentId(Category.ROOT);
        categoryAutoRepo.save(category);

        mockMvc.perform(MockUtils.populateGetBuilder("/api/category").cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.[0].name").value(category.getName()))
                .andExpect(jsonPath("$.data.[0].url").value(category.getUrl()))
                .andExpect(jsonPath("$.data.[0].sort").value(category.getSort()))
                .andExpect(jsonPath("$.data.[0].description").value(category.getDescription()))
                .andExpect(jsonPath("$.code").value("0"));
    }
}