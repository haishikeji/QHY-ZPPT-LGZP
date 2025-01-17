package cn.px.job.company.service;

import java.util.List;

import cn.px.job.company.domain.ShCompanyRecruitVideo;

/**
 * 企业招聘记录视频Service接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface IShCompanyRecruitVideoService 
{
    /**
     * 查询企业招聘记录视频
     * 
     * @param id 企业招聘记录视频主键
     * @return 企业招聘记录视频
     */
    public ShCompanyRecruitVideo selectShCompanyRecruitVideoById(Long id);

    /**
     * 查询企业招聘记录视频列表
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 企业招聘记录视频集合
     */
    public List<ShCompanyRecruitVideo> selectShCompanyRecruitVideoList(ShCompanyRecruitVideo shCompanyRecruitVideo);

    /**
     * 新增企业招聘记录视频
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 结果
     */
    public int insertShCompanyRecruitVideo(ShCompanyRecruitVideo shCompanyRecruitVideo);

    /**
     * 修改企业招聘记录视频
     * 
     * @param shCompanyRecruitVideo 企业招聘记录视频
     * @return 结果
     */
    public int updateShCompanyRecruitVideo(ShCompanyRecruitVideo shCompanyRecruitVideo);

    /**
     * 批量删除企业招聘记录视频
     * 
     * @param ids 需要删除的企业招聘记录视频主键集合
     * @return 结果
     */
    public int deleteShCompanyRecruitVideoByIds(Long[] ids);

    /**
     * 删除企业招聘记录视频信息
     * 
     * @param id 企业招聘记录视频主键
     * @return 结果
     */
    public int deleteShCompanyRecruitVideoById(Long id);


    /**
     * 删除企业招聘记录视频信息
     *
     * @param id 企业招聘记录视频主键
     * @return 结果
     */
    public int deleteShCompanyRecruitVideoByRecruitId(Long id);
}
