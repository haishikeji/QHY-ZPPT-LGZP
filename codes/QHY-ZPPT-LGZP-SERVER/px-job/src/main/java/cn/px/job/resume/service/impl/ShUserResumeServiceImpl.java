package cn.px.job.resume.service.impl;

import cn.px.common.utils.DateUtils;
import cn.px.job.resume.domain.ShUserResume;
import cn.px.job.resume.mapper.ShUserResumeMapper;
import cn.px.job.resume.service.IShUserResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户在线简历Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ShUserResumeServiceImpl implements IShUserResumeService
{
    @Autowired
    private ShUserResumeMapper shUserResumeMapper;

    /**
     * 查询用户在线简历
     *
     * @param id 用户在线简历主键
     * @return 用户在线简历
     */
    @Override
    public ShUserResume selectShUserResumeById(Long id)
    {
        return shUserResumeMapper.selectShUserResumeById(id);
    }

    /**
     * 查询用户在线简历列表
     *
     * @param shUserResume 用户在线简历
     * @return 用户在线简历
     */
    @Override
    public List<ShUserResume> selectShUserResumeList(ShUserResume shUserResume)
    {

        return shUserResumeMapper.selectShUserResumeList(shUserResume);
    }
    @Override
    public List<ShUserResume> selectShUserResumeListNew(ShUserResume shUserResume)
    {
        return shUserResumeMapper.selectShUserResumeListNew(shUserResume);
    }

    /**
     * 新增用户在线简历
     *
     * @param shUserResume 用户在线简历
     * @return 结果
     */
    @Override
    public int insertShUserResume(ShUserResume shUserResume)
    {
        shUserResume.setCreateTime(DateUtils.getNowDate());
        return shUserResumeMapper.insertShUserResume(shUserResume);
    }

    /**
     * 修改用户在线简历
     *
     * @param shUserResume 用户在线简历
     * @return 结果
     */
    @Override
    public int updateShUserResume(ShUserResume shUserResume)
    {
        shUserResume.setUpdateTime(DateUtils.getNowDate());
        return shUserResumeMapper.updateShUserResume(shUserResume);
    }

    /**
     * 批量删除用户在线简历
     *
     * @param ids 需要删除的用户在线简历主键
     * @return 结果
     */
    @Override
    public int deleteShUserResumeByIds(Long[] ids)
    {
        return shUserResumeMapper.deleteShUserResumeByIds(ids);
    }

    /**
     * 删除用户在线简历信息
     *
     * @param id 用户在线简历主键
     * @return 结果
     */
    @Override
    public int deleteShUserResumeById(Long id)
    {
        return shUserResumeMapper.deleteShUserResumeById(id);
    }

    /**
     * 根据userid删除用户在线简历信息
     *
     * @param userId userid
     * @return 结果
     */
    @Override
    public int deleteShUserResumeByUserId(Long userId) {
        return shUserResumeMapper.deleteShUserResumeByUserId(userId);
    }


    /**
     * 根据userId查询用户在线简历
     *
     * @param userId 用户在线简历userId
     * @return 结果
     */
    @Override
    public ShUserResume selectShUserResumeByUserId(Long userId)
    {
        return shUserResumeMapper.selectShUserResumeByUserId(userId);
    }

    @Override
    public ShUserResume checkUserResumeIsShow(ShUserResume shUserResume) {
        return shUserResumeMapper.checkUserResumeIsShow(shUserResume);
    }

    @Override
    public List<Long> selectIsNotShowResumeUserIdList(ShUserResume shUserResume) {
        return shUserResumeMapper.selectIsNotShowResumeUserIdList(shUserResume);
    }

    /**
     * 只查询userResume一张表
     * @return
     */
    @Override
    public ShUserResume selectResumeInfo(ShUserResume shUserResume) {
        return shUserResumeMapper.selectResumeInfo(shUserResume);
    }


}
