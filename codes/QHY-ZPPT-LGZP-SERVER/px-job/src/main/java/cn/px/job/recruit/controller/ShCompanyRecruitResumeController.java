package cn.px.job.recruit.controller;

import java.util.List;

import cn.px.job.recruit.domain.ShCompanyRecruitResume;
import cn.px.job.recruit.service.IShCompanyRecruitResumeService;
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
 * 招聘记录附件Controller
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/recruitresume/recruitresume")
public class ShCompanyRecruitResumeController extends BaseController
{
    @Autowired
    private IShCompanyRecruitResumeService shCompanyRecruitResumeService;

    /**
     * 查询招聘记录附件列表
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShCompanyRecruitResume shCompanyRecruitResume)
    {
        startPage();
        List<ShCompanyRecruitResume> list = shCompanyRecruitResumeService.selectShCompanyRecruitResumeList(shCompanyRecruitResume);
        return getDataTable(list);
    }

    /**
     * 导出招聘记录附件列表
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:export')")
    @Log(title = "招聘记录附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShCompanyRecruitResume shCompanyRecruitResume)
    {
        List<ShCompanyRecruitResume> list = shCompanyRecruitResumeService.selectShCompanyRecruitResumeList(shCompanyRecruitResume);
        ExcelUtil<ShCompanyRecruitResume> util = new ExcelUtil<ShCompanyRecruitResume>(ShCompanyRecruitResume.class);
        return util.exportExcel(list, "招聘记录附件数据");
    }

    /**
     * 获取招聘记录附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(shCompanyRecruitResumeService.selectShCompanyRecruitResumeById(id));
    }

    /**
     * 新增招聘记录附件
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:add')")
    @Log(title = "招聘记录附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShCompanyRecruitResume shCompanyRecruitResume)
    {
        return toAjax(shCompanyRecruitResumeService.insertShCompanyRecruitResume(shCompanyRecruitResume));
    }

    /**
     * 修改招聘记录附件
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:edit')")
    @Log(title = "招聘记录附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShCompanyRecruitResume shCompanyRecruitResume)
    {
        return toAjax(shCompanyRecruitResumeService.updateShCompanyRecruitResume(shCompanyRecruitResume));
    }

    /**
     * 删除招聘记录附件
     */
    @PreAuthorize("@ss.hasPermi('recruitresume:recruitresume:remove')")
    @Log(title = "招聘记录附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shCompanyRecruitResumeService.deleteShCompanyRecruitResumeByIds(ids));
    }
}
