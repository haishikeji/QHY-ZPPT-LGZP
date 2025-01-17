package cn.px.job.recruit.controller;

import java.util.List;

import cn.px.job.recruit.domain.ShCompanyRecruitJobType;
import cn.px.job.recruit.service.IShCompanyRecruitJobTypeService;
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
 * 招聘工作性质Controller
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/recruitjobtype/recruitjobtype")
public class ShCompanyRecruitJobTypeController extends BaseController
{
    @Autowired
    private IShCompanyRecruitJobTypeService shCompanyRecruitJobTypeService;

    /**
     * 查询招聘工作性质列表
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShCompanyRecruitJobType shCompanyRecruitJobType)
    {
        startPage();
        List<ShCompanyRecruitJobType> list = shCompanyRecruitJobTypeService.selectShCompanyRecruitJobTypeList(shCompanyRecruitJobType);
        return getDataTable(list);
    }

    /**
     * 导出招聘工作性质列表
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:export')")
    @Log(title = "招聘工作性质", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShCompanyRecruitJobType shCompanyRecruitJobType)
    {
        List<ShCompanyRecruitJobType> list = shCompanyRecruitJobTypeService.selectShCompanyRecruitJobTypeList(shCompanyRecruitJobType);
        ExcelUtil<ShCompanyRecruitJobType> util = new ExcelUtil<ShCompanyRecruitJobType>(ShCompanyRecruitJobType.class);
        return util.exportExcel(list, "招聘工作性质数据");
    }

    /**
     * 获取招聘工作性质详细信息
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(shCompanyRecruitJobTypeService.selectShCompanyRecruitJobTypeById(id));
    }

    /**
     * 新增招聘工作性质
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:add')")
    @Log(title = "招聘工作性质", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShCompanyRecruitJobType shCompanyRecruitJobType)
    {
        return toAjax(shCompanyRecruitJobTypeService.insertShCompanyRecruitJobType(shCompanyRecruitJobType));
    }

    /**
     * 修改招聘工作性质
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:edit')")
    @Log(title = "招聘工作性质", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShCompanyRecruitJobType shCompanyRecruitJobType)
    {
        return toAjax(shCompanyRecruitJobTypeService.updateShCompanyRecruitJobType(shCompanyRecruitJobType));
    }

    /**
     * 删除招聘工作性质
     */
    @PreAuthorize("@ss.hasPermi('recruitjobtype:recruitjobtype:remove')")
    @Log(title = "招聘工作性质", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shCompanyRecruitJobTypeService.deleteShCompanyRecruitJobTypeByIds(ids));
    }
}
