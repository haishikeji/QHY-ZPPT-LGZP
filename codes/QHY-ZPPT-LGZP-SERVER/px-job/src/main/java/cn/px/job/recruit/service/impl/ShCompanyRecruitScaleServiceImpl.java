package cn.px.job.recruit.service.impl;

import cn.px.common.utils.DateUtils;
import cn.px.job.recruit.domain.ShCompanyRecruit;
import cn.px.job.company.domain.ShCompanyScale;
import cn.px.job.recruit.mapper.ShCompanyRecruitScaleMapper;
import cn.px.job.recruit.service.IShCompanyRecruitScaleService;
import cn.px.job.recruit.domain.ShCompanyRecruitScale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 招聘纪录与公司规模中间Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ShCompanyRecruitScaleServiceImpl implements IShCompanyRecruitScaleService
{
    @Autowired
    private ShCompanyRecruitScaleMapper shCompanyRecruitScaleMapper;

    /**
     * 查询招聘纪录与公司规模中间
     *
     * @param id 招聘纪录与公司规模中间主键
     * @return 招聘纪录与公司规模中间
     */
    @Override
    public ShCompanyRecruitScale selectShCompanyRecruitScaleById(Long id)
    {
        return shCompanyRecruitScaleMapper.selectShCompanyRecruitScaleById(id);
    }

    /**
     * 查询招聘纪录与公司规模中间列表
     *
     * @param shCompanyRecruitScale 招聘纪录与公司规模中间
     * @return 招聘纪录与公司规模中间
     */
    @Override
    public List<ShCompanyRecruitScale> selectShCompanyRecruitScaleList(ShCompanyRecruitScale shCompanyRecruitScale)
    {
        return shCompanyRecruitScaleMapper.selectShCompanyRecruitScaleList(shCompanyRecruitScale);
    }

    /**
     * 新增招聘纪录与公司规模中间
     *
     * @param shCompanyRecruitScale 招聘纪录与公司规模中间
     * @return 结果
     */
    @Override
    public int insertShCompanyRecruitScale(ShCompanyRecruitScale shCompanyRecruitScale)
    {
        shCompanyRecruitScale.setCreateTime(DateUtils.getNowDate());
        return shCompanyRecruitScaleMapper.insertShCompanyRecruitScale(shCompanyRecruitScale);
    }

    /**
     * 修改招聘纪录与公司规模中间
     *
     * @param shCompanyRecruitScale 招聘纪录与公司规模中间
     * @return 结果
     */
    @Override
    public int updateShCompanyRecruitScale(ShCompanyRecruitScale shCompanyRecruitScale)
    {
        shCompanyRecruitScale.setUpdateTime(DateUtils.getNowDate());
        return shCompanyRecruitScaleMapper.updateShCompanyRecruitScale(shCompanyRecruitScale);
    }

    /**
     * 批量删除招聘纪录与公司规模中间(逻辑删除)
     *
     * @param ids 需要删除的招聘纪录与公司规模中间主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitScaleByIds(Long[] ids)
    {
        return shCompanyRecruitScaleMapper.deleteShCompanyRecruitScaleByIds(ids);
    }

    /**
     * 批量删除招聘纪录与公司规模中间(物理删除)
     *
     * @param ids 需要删除的招聘纪录与公司规模中间主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitScaleByIds2(Long[] ids)
    {
        return shCompanyRecruitScaleMapper.deleteShCompanyRecruitScaleByIds2(ids);
    }

    /**
     * 删除招聘纪录与公司规模中间信息
     *
     * @param id 招聘纪录与公司规模中间主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitScaleByRecruitId(Long id)
    {
        return shCompanyRecruitScaleMapper.deleteShCompanyRecruitScaleByRecruitId(id);
    }

    /**
     * 删除招聘纪录与公司规模中间信息
     *
     * @param id 招聘纪录与公司规模中间主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitScaleById(Long id)
    {
        return shCompanyRecruitScaleMapper.deleteShCompanyRecruitScaleById(id);
    }
    @Override
    public int deleteShCompanyRecruitScaleById2(Long id)
    {
        return shCompanyRecruitScaleMapper.deleteShCompanyRecruitScaleById2(id);
    }



    @Override
    public int saveScale(ShCompanyRecruit companyRecruit) {
        ShCompanyScale compnayScale = companyRecruit.getShCompanyScale();
        ShCompanyRecruitScale companyRecruitScale = new ShCompanyRecruitScale();
        companyRecruitScale.setCompanyId(companyRecruit.getCompanyId());
        companyRecruitScale.setRecruitId(companyRecruit.getId());
        companyRecruitScale.setScaleId(compnayScale.getId());
        return shCompanyRecruitScaleMapper.insertShCompanyRecruitScale(companyRecruitScale);
    }
}
