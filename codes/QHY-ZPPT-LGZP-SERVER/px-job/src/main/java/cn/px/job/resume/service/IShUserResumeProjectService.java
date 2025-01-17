package cn.px.job.resume.service;

import java.util.List;

import cn.px.job.resume.domain.ShUserResumeProject;

/**
 * 求职者在线简历与项目经历中间Service接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface IShUserResumeProjectService 
{
    /**
     * 查询求职者在线简历与项目经历中间
     * 
     * @param id 求职者在线简历与项目经历中间主键
     * @return 求职者在线简历与项目经历中间
     */
    public ShUserResumeProject selectShUserResumeProjectById(Long id);

    /**
     * 查询求职者在线简历与项目经历中间列表
     * 
     * @param shUserResumeProject 求职者在线简历与项目经历中间
     * @return 求职者在线简历与项目经历中间集合
     */
    public List<ShUserResumeProject> selectShUserResumeProjectList(ShUserResumeProject shUserResumeProject);

    /**
     * 新增求职者在线简历与项目经历中间
     * 
     * @param shUserResumeProject 求职者在线简历与项目经历中间
     * @return 结果
     */
    public int insertShUserResumeProject(ShUserResumeProject shUserResumeProject);

    /**
     * 修改求职者在线简历与项目经历中间
     * 
     * @param shUserResumeProject 求职者在线简历与项目经历中间
     * @return 结果
     */
    public int updateShUserResumeProject(ShUserResumeProject shUserResumeProject);

    /**
     * 批量删除求职者在线简历与项目经历中间
     * 
     * @param ids 需要删除的求职者在线简历与项目经历中间主键集合
     * @return 结果
     */
    public int deleteShUserResumeProjectByIds(Long[] ids);

    /**
     * 删除求职者在线简历与项目经历中间信息
     * 
     * @param id 求职者在线简历与项目经历中间主键
     * @return 结果
     */
    public int deleteShUserResumeProjectById(Long id);


    /**
     * 根据在线简历表id和项目经历表id删除求职者在线简历与项目经历中间信息
     *
     * @param shUserResumeProject
     * @return 结果
     */
    public int deleteShUserResumeProjectByResumeIdAndProjectId(ShUserResumeProject shUserResumeProject);
}
