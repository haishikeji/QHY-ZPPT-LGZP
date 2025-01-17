package cn.px.job.category.controller;

import java.util.List;

import cn.px.job.category.domain.ShForumCategory;
import cn.px.job.category.service.IShForumCategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.px.common.annotation.Log;
import cn.px.common.core.controller.BaseController;
import cn.px.common.core.domain.AjaxResult;
import cn.px.common.enums.BusinessType;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 论坛分类Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/category/category")
public class ShForumCategoryController extends BaseController
{
    @Autowired
    private IShForumCategoryService shForumCategoryService;

    /**
     * 查询论坛分类列表
     */
    @PreAuthorize("@ss.hasPermi('category:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShForumCategory shForumCategory)
    {
        startPage();
        List<ShForumCategory> list = shForumCategoryService.selectShForumCategoryList(shForumCategory);
        return getDataTable(list);
    }

    /**
     * 导出论坛分类列表
     */
    @PreAuthorize("@ss.hasPermi('category:category:export')")
    @Log(title = "论坛分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShForumCategory shForumCategory)
    {
        List<ShForumCategory> list = shForumCategoryService.selectShForumCategoryList(shForumCategory);
        ExcelUtil<ShForumCategory> util = new ExcelUtil<ShForumCategory>(ShForumCategory.class);
        return util.exportExcel(list, "论坛分类数据");
    }

    /**
     * 获取论坛分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('category:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(shForumCategoryService.selectShForumCategoryById(id));
    }

    /**
     * 新增论坛分类
     */
    @PreAuthorize("@ss.hasPermi('category:category:add')")
    @Log(title = "论坛分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShForumCategory shForumCategory)
    {
        List<ShForumCategory> list = shForumCategoryService.selectShForumCategoryList(shForumCategory);
        if(list!=null&&list.size()>0){
            return AjaxResult.error("分类已存在，请勿重复添加！");
        }
        shForumCategory.setCreateBy(getUsername());
        return toAjax(shForumCategoryService.insertShForumCategory(shForumCategory));
    }

    /**
     * 修改论坛分类
     */
    @PreAuthorize("@ss.hasPermi('category:category:edit')")
    @Log(title = "论坛分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShForumCategory shForumCategory)
    {
        ShForumCategory forumCategory = shForumCategoryService.selectShForumCategoryById(shForumCategory.getId());
        if (!shForumCategory.getType().equals(forumCategory.getType())) {
            ShForumCategory shForumCategory1 = new ShForumCategory();
            shForumCategory1.setType(shForumCategory.getType());
            List<ShForumCategory> list = shForumCategoryService.selectShForumCategoryList(shForumCategory1);
            if (list != null && list.size() > 0) {
                return AjaxResult.error("分类已存在");
            }
        }
        shForumCategory.setUpdateBy(getUsername());
        return toAjax(shForumCategoryService.updateShForumCategory(shForumCategory));
    }

    /**
     * 删除论坛分类
     */
    @PreAuthorize("@ss.hasPermi('category:category:remove')")
    @Log(title = "论坛分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shForumCategoryService.deleteShForumCategoryByIds(ids));
    }
}
