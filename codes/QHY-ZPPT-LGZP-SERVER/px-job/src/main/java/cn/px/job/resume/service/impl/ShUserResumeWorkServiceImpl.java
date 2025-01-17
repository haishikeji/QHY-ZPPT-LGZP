package cn.px.job.resume.service.impl;

import java.util.List;
import cn.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.job.resume.mapper.ShUserResumeWorkMapper;
import cn.px.job.resume.domain.ShUserResumeWork;
import cn.px.job.resume.service.IShUserResumeWorkService;

/**
 * 求职者在线简历与工作经历中间Service业务层处理
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ShUserResumeWorkServiceImpl implements IShUserResumeWorkService 
{
    @Autowired
    private ShUserResumeWorkMapper shUserResumeWorkMapper;

    /**
     * 查询求职者在线简历与工作经历中间
     * 
     * @param id 求职者在线简历与工作经历中间主键
     * @return 求职者在线简历与工作经历中间
     */
    @Override
    public ShUserResumeWork selectShUserResumeWorkById(Long id)
    {
        return shUserResumeWorkMapper.selectShUserResumeWorkById(id);
    }

    /**
     * 查询求职者在线简历与工作经历中间列表
     * 
     * @param shUserResumeWork 求职者在线简历与工作经历中间
     * @return 求职者在线简历与工作经历中间
     */
    @Override
    public List<ShUserResumeWork> selectShUserResumeWorkList(ShUserResumeWork shUserResumeWork)
    {
        return shUserResumeWorkMapper.selectShUserResumeWorkList(shUserResumeWork);
    }

    /**
     * 新增求职者在线简历与工作经历中间
     * 
     * @param shUserResumeWork 求职者在线简历与工作经历中间
     * @return 结果
     */
    @Override
    public int insertShUserResumeWork(ShUserResumeWork shUserResumeWork)
    {
        shUserResumeWork.setCreateTime(DateUtils.getNowDate());
        return shUserResumeWorkMapper.insertShUserResumeWork(shUserResumeWork);
    }

    /**
     * 修改求职者在线简历与工作经历中间
     * 
     * @param shUserResumeWork 求职者在线简历与工作经历中间
     * @return 结果
     */
    @Override
    public int updateShUserResumeWork(ShUserResumeWork shUserResumeWork)
    {
        shUserResumeWork.setUpdateTime(DateUtils.getNowDate());
        return shUserResumeWorkMapper.updateShUserResumeWork(shUserResumeWork);
    }

    /**
     * 批量删除求职者在线简历与工作经历中间
     * 
     * @param ids 需要删除的求职者在线简历与工作经历中间主键
     * @return 结果
     */
    @Override
    public int deleteShUserResumeWorkByIds(Long[] ids)
    {
        return shUserResumeWorkMapper.deleteShUserResumeWorkByIds(ids);
    }

    /**
     * 删除求职者在线简历与工作经历中间信息
     * 
     * @param id 求职者在线简历与工作经历中间主键
     * @return 结果
     */
    @Override
    public int deleteShUserResumeWorkById(Long id)
    {
        return shUserResumeWorkMapper.deleteShUserResumeWorkById(id);
    }

    @Override
    public int deleteShUserResumeWorkByResumeIdAndWorkId(ShUserResumeWork shUserResumeWork) {
        return shUserResumeWorkMapper.deleteShUserResumeWorkByResumeIdAndWorkId(shUserResumeWork);
    }


}
