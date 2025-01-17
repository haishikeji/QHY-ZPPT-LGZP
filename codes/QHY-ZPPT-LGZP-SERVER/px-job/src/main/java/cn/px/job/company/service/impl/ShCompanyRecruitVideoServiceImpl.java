package cn.px.job.company.service.impl;

import java.util.List;
import cn.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.job.company.mapper.ShCompanyRecruitVideoMapper;
import cn.px.job.company.domain.ShCompanyRecruitVideo;
import cn.px.job.company.service.IShCompanyRecruitVideoService;

/**
 * 企业招聘记录视频Service业务层处理
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ShCompanyRecruitVideoServiceImpl implements IShCompanyRecruitVideoService 
{
    @Autowired
    private ShCompanyRecruitVideoMapper shCompanyRecruitVideoMapper;

    /**
     * 查询企业招聘记录视频
     * 
     * @param id 企业招聘记录视频主键
     * @return 企业招聘记录视频
     */
    @Override
    public ShCompanyRecruitVideo selectShCompanyRecruitVideoById(Long id)
    {
        return shCompanyRecruitVideoMapper.selectShCompanyRecruitVideoById(id);
    }

    /**
     * 查询企业招聘记录视频列表
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 企业招聘记录视频
     */
    @Override
    public List<ShCompanyRecruitVideo> selectShCompanyRecruitVideoList(ShCompanyRecruitVideo shCompanyRecruitVideo)
    {
        return shCompanyRecruitVideoMapper.selectShCompanyRecruitVideoList(shCompanyRecruitVideo);
    }

    /**
     * 新增企业招聘记录视频
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 结果
     */
    @Override
    public int insertShCompanyRecruitVideo(ShCompanyRecruitVideo shCompanyRecruitVideo)
    {
        shCompanyRecruitVideo.setCreateTime(DateUtils.getNowDate());
        return shCompanyRecruitVideoMapper.insertShCompanyRecruitVideo(shCompanyRecruitVideo);
    }

    /**
     * 修改企业招聘记录视频
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 结果
     */
    @Override
    public int updateShCompanyRecruitVideo(ShCompanyRecruitVideo shCompanyRecruitVideo)
    {
        shCompanyRecruitVideo.setUpdateTime(DateUtils.getNowDate());
        return shCompanyRecruitVideoMapper.updateShCompanyRecruitVideo(shCompanyRecruitVideo);
    }

    /**
     * 批量删除企业招聘记录视频
     * 
     * @param ids 需要删除的企业招聘记录视频主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitVideoByIds(Long[] ids)
    {
        return shCompanyRecruitVideoMapper.deleteShCompanyRecruitVideoByIds(ids);
    }

    /**
     * 删除企业招聘记录视频信息
     * 
     * @param id 企业招聘记录视频主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitVideoById(Long id)
    {
        return shCompanyRecruitVideoMapper.deleteShCompanyRecruitVideoById(id);
    }

    /**
     * 删除企业招聘记录视频信息
     *
     * @param id 企业招聘记录视频主键
     * @return 结果
     */
    @Override
    public int deleteShCompanyRecruitVideoByRecruitId(Long id)
    {
        return shCompanyRecruitVideoMapper.deleteShCompanyRecruitVideoByRecruitId(id);
    }
}
