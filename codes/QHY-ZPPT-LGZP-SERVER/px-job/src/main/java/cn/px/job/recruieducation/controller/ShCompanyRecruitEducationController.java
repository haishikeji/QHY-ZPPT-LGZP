package cn.px.job.recruieducation.controller;

import java.util.List;

import cn.px.job.recruieducation.domain.ShCompanyRecruitEducation;
import cn.px.job.recruieducation.service.IShCompanyRecruitEducationService;
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
 * 招聘纪录与学历中间Controller
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/recruieducation/recruieducation")
public class ShCompanyRecruitEducationController extends BaseController
{
    @Autowired
    private IShCompanyRecruitEducationService shCompanyRecruitEducationService;

    /**
     * 查询招聘纪录与学历中间列表
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShCompanyRecruitEducation shCompanyRecruitEducation)
    {
        startPage();
        List<ShCompanyRecruitEducation> list = shCompanyRecruitEducationService.selectShCompanyRecruitEducationList(shCompanyRecruitEducation);
        return getDataTable(list);
    }

    /**
     * 导出招聘纪录与学历中间列表
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:export')")
    @Log(title = "招聘纪录与学历中间", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShCompanyRecruitEducation shCompanyRecruitEducation)
    {
        List<ShCompanyRecruitEducation> list = shCompanyRecruitEducationService.selectShCompanyRecruitEducationList(shCompanyRecruitEducation);
        ExcelUtil<ShCompanyRecruitEducation> util = new ExcelUtil<ShCompanyRecruitEducation>(ShCompanyRecruitEducation.class);
        return util.exportExcel(list, "招聘纪录与学历中间数据");
    }

    /**
     * 获取招聘纪录与学历中间详细信息
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(shCompanyRecruitEducationService.selectShCompanyRecruitEducationById(id));
    }

    /**
     * 新增招聘纪录与学历中间
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:add')")
    @Log(title = "招聘纪录与学历中间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShCompanyRecruitEducation shCompanyRecruitEducation)
    {
        return toAjax(shCompanyRecruitEducationService.insertShCompanyRecruitEducation(shCompanyRecruitEducation));
    }

    /**
     * 修改招聘纪录与学历中间
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:edit')")
    @Log(title = "招聘纪录与学历中间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShCompanyRecruitEducation shCompanyRecruitEducation)
    {
        return toAjax(shCompanyRecruitEducationService.updateShCompanyRecruitEducation(shCompanyRecruitEducation));
    }

    /**
     * 删除招聘纪录与学历中间
     */
    @PreAuthorize("@ss.hasPermi('recruieducation:recruieducation:remove')")
    @Log(title = "招聘纪录与学历中间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shCompanyRecruitEducationService.deleteShCompanyRecruitEducationByIds(ids));
    }
}
